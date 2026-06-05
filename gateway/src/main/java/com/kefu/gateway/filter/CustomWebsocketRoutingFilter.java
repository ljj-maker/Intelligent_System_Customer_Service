package com.kefu.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.headers.HttpHeadersFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.socket.CloseStatus;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.*;

@Slf4j
@Component
public class CustomWebsocketRoutingFilter implements GlobalFilter, Ordered {

    //Sec-Websocket protocol
    public static final String SEC_WEBSOCKET_PROTOCOL = "Sec-WebSocket-Protocol";
    //Sec-Websocket header
    public static final String SEC_WEBSOCKET_HEADER = "sec-websocket";
    //http header schema
    public static final String HEADER_UPGRADE_WebSocket = "websocket";
    public static final String HEADER_UPGRADE_HTTP = "http";
    public static final String HEADER_UPGRADE_HTTPS = "https";
    private final WebSocketClient webSocketClient;
    private final WebSocketService webSocketService;
    private final ObjectProvider<List<HttpHeadersFilter>> headersFiltersProvider;
    // 不直接使用 headersFilters 用该变量代替
    private volatile List<HttpHeadersFilter> headersFilters;

    public CustomWebsocketRoutingFilter(WebSocketClient webSocketClient, WebSocketService webSocketService, ObjectProvider<List<HttpHeadersFilter>> headersFiltersProvider) {
        this.webSocketClient = webSocketClient;
        this.webSocketService = webSocketService;
        this.headersFiltersProvider = headersFiltersProvider;
    }

    /* for testing */
    //http请求转为ws请求
    static String convertHttpToWs(String scheme) {
        scheme = scheme.toLowerCase();
        return "http".equals(scheme) ? "ws" : "https".equals(scheme) ? "wss" : scheme;
    }

    @Override
    public int getOrder() {
        // Before NettyRoutingFilter since this routes certain http requests
        //修改了这里 之前是-1 降低优先级
        return Ordered.LOWEST_PRECEDENCE - 2;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        changeSchemeIfIsWebSocketUpgrade(exchange);

        URI requestUrl = exchange.getRequiredAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        String scheme = requestUrl.getScheme();

        if (ServerWebExchangeUtils.isAlreadyRouted(exchange) || (!"ws".equals(scheme) && !"wss".equals(scheme))) {
            return chain.filter(exchange);
        }
        ServerWebExchangeUtils.setAlreadyRouted(exchange);

        HttpHeaders headers = exchange.getRequest().getHeaders();
        HttpHeaders filtered = HttpHeadersFilter.filterRequest(getHeadersFilters(), exchange);

        List<String> protocols = getProtocols(headers);

        return this.webSocketService.handleRequest(exchange, new ProxyWebSocketHandler(requestUrl, this.webSocketClient, filtered, protocols));
    }

    /* for testing */
    //获取请求头里的协议信息
    List<String> getProtocols(HttpHeaders headers) {
        List<String> protocols = headers.get(SEC_WEBSOCKET_PROTOCOL);
        if (protocols != null) {
            ArrayList<String> updatedProtocols = new ArrayList<>();
            for (int i = 0; i < protocols.size(); i++) {
                String protocol = protocols.get(i);
                updatedProtocols.addAll(Arrays.asList(StringUtils.tokenizeToStringArray(protocol, ",")));
            }
            protocols = updatedProtocols;
        }
        return protocols;
    }

    /* for testing */
    List<HttpHeadersFilter> getHeadersFilters() {
        if (this.headersFilters == null) {
            this.headersFilters = this.headersFiltersProvider.getIfAvailable(ArrayList::new);

            // remove host header unless specifically asked not to
            this.headersFilters.add((headers, exchange) -> {
                HttpHeaders filtered = new HttpHeaders();
                filtered.addAll(headers);
                filtered.remove(HttpHeaders.HOST);
                boolean preserveHost = exchange.getAttributeOrDefault(ServerWebExchangeUtils.PRESERVE_HOST_HEADER_ATTRIBUTE, false);
                if (preserveHost) {
                    String host = exchange.getRequest().getHeaders().getFirst(HttpHeaders.HOST);
                    filtered.add(HttpHeaders.HOST, host);
                }
                return filtered;
            });

            this.headersFilters.add((headers, exchange) -> {
                HttpHeaders filtered = new HttpHeaders();
                for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                    if (!entry.getKey().toLowerCase().startsWith(SEC_WEBSOCKET_HEADER)) {
                        filtered.addAll(entry.getKey(), entry.getValue());
                    }
                }
                return filtered;
            });
        }

        return this.headersFilters;
    }

    static void changeSchemeIfIsWebSocketUpgrade(ServerWebExchange exchange) {
        // 检查版本是否适合
        URI requestUrl = exchange.getRequiredAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        String scheme = requestUrl.getScheme().toLowerCase();
        String upgrade = exchange.getRequest().getHeaders().getUpgrade();
        // change the scheme if the socket client send a "http" or "https"
        if (HEADER_UPGRADE_WebSocket.equalsIgnoreCase(upgrade) && (HEADER_UPGRADE_HTTP.equals(scheme) || HEADER_UPGRADE_HTTPS.equals(scheme))) {
            String wsScheme = convertHttpToWs(scheme);
            boolean encoded = ServerWebExchangeUtils.containsEncodedParts(requestUrl);
            URI wsRequestUrl = UriComponentsBuilder.fromUri(requestUrl).scheme(wsScheme).build(encoded).toUri();
            exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, wsRequestUrl);
            if (log.isTraceEnabled()) {
                log.trace("changeSchemeTo:[" + wsRequestUrl + "]");
            }
        }
    }

    //自定义websocket处理方式
    private static class ProxyWebSocketHandler implements WebSocketHandler {

        private final WebSocketClient client;
        private final URI url;
        private final HttpHeaders headers;
        private final List<String> subProtocols;

        ProxyWebSocketHandler(URI url, WebSocketClient client, HttpHeaders headers, List<String> protocols) {
            this.client = client;
            this.url = url;
            this.headers = headers;
            if (protocols != null) {
                this.subProtocols = protocols;
            } else {
                this.subProtocols = Collections.emptyList();
            }
        }

        @Override
        public List<String> getSubProtocols() {
            return this.subProtocols;
        }

        @Override
        public Mono<Void> handle(WebSocketSession session) {
            return this.client.execute(this.url, this.headers, new WebSocketHandler() {
                private CloseStatus adaptCloseStatus(CloseStatus closeStatus) {
                    int code = closeStatus.getCode();
                    if (code > 2999 && code < 5000) {
                        return closeStatus;
                    }
                    switch (code) {
                        case 1000:
                            //正常关闭
                            return closeStatus;
                        case 1001:
                            //服务器挂了或者页面跳转
                            return closeStatus;
                        case 1002:
                            //协议错误
                            return closeStatus;
                        case 1003:
                            //收到了不能处理的数据类型
                            return closeStatus;
                        case 1004:
                            // 预留关闭状态码
                            return CloseStatus.PROTOCOL_ERROR;
                        case 1005:
                            // 预留关闭状态码 期望收到状态码但是没有收到
                            return CloseStatus.PROTOCOL_ERROR;
                        case 1006:
                            // 预留关闭状态码 连接异常关闭
                            return CloseStatus.PROTOCOL_ERROR;
                        case 1007:
                            //收到的数据与实际的消息类型不匹配
                            return closeStatus;
                        case 1008:
                            //收到不符合规则的消息
                            return closeStatus;
                        case 1009:
                            //收到太大的不能处理的消息
                            return closeStatus;
                        case 1010:
                            //client希望server提供多个扩展，server没有返回相应的扩展信息
                            return closeStatus;
                        case 1011:
                            //server遇到不能完成的请求
                            return closeStatus;
                        case 1012:
                            // Not in RFC6455
                            // return CloseStatus.SERVICE_RESTARTED;
                            return CloseStatus.PROTOCOL_ERROR;
                        case 1013:
                            // Not in RFC6455
                            // return CloseStatus.SERVICE_OVERLOAD;
                            return CloseStatus.PROTOCOL_ERROR;
                        case 1015:
                            // 不能进行TLS握手 如：server证书不能验证
                            return CloseStatus.PROTOCOL_ERROR;
                        default:
                            return CloseStatus.PROTOCOL_ERROR;
                    }
                }

                /**
                 * send      发送传出消息
                 * receive   处理入站消息流
                 * doOnNext  对每条消息做什么
                 * zip       加入流
                 * then      返回接收完成时完成的Mono<Void>
                 */
                @Override
                public Mono<Void> handle(WebSocketSession proxySession) {
                    Mono<Void> serverClose = proxySession.closeStatus().filter(__ -> session.isOpen())
                            .map(this::adaptCloseStatus)
                            .flatMap(session::close);
                    Mono<Void> proxyClose = session.closeStatus().filter(__ -> proxySession.isOpen())
                            .map(this::adaptCloseStatus)
                            .flatMap(proxySession::close);
                    // Use retain() for Reactor Netty
                    Mono<Void> proxySessionSend = proxySession
                            .send(session.receive().doOnNext(WebSocketMessage::retain));
                    Mono<Void> serverSessionSend = session
                            .send(proxySession.receive().doOnNext(WebSocketMessage::retain));
                    // Ensure closeStatus from one propagates to the other
                    Mono.when(serverClose, proxyClose).subscribe();
                    // Complete when both sessions are done
                    return Mono.zip(proxySessionSend, serverSessionSend).then();
                }
                @Override
                public List<String> getSubProtocols() {
                    return CustomWebsocketRoutingFilter.ProxyWebSocketHandler.this.subProtocols;
                }
            });
        }
    }


}