# ICSS - 智能客服系统 (Intelligent Customer Service System)

&gt; 基于 Spring Boot + Vue 的在线客服解决方案，支持用户咨询、智能机器应答与人工客服实时转接。

---

## 一、项目简介

ICSS 是一套面向企业客服场景的 Web 端即时通讯系统。用户进入页面后可先与机器人进行对话，当需要人工介入时，系统会自动分配在线客服，建立实时双向通信。

项目采用 **Maven 多模块** 结构，后端基于 Spring Boot 构建，通过 WebSocket 实现长连接消息推送，结合 Redis 维护客服在线状态并支持用户进线自动分配，使用 JWT 解析账号身份，通过 ThreadLocal 保存消息发送方 ID 用于会话识别。

---

## 二、功能特性

### 用户端
- 匿名/登录后进入咨询页面
- 与智能机器人进行基础问答
- 发起"转人工"请求，系统自动分配在线客服
- 实时收发文本消息，查看历史对话记录

### 客服端
- 客服登录后自动建立 WebSocket 连接
- 实时接收系统分配的用户会话
- 同时处理多个用户对话（多标签切换）
- 查看用户基本信息与历史咨询记录

### 机器人模块
- 基于规则/关键词匹配进行自动回复
- 无法回答时引导用户转接人工

### 管理功能
- 用户注册与登录（JWT 鉴权）
- 客服账号管理
- 对话记录查询与分页展示

---

## 三、技术架构

### 后端技术栈
| 技术 | 说明 |
|------|------|
| Spring Boot | 基础开发框架 |
| Spring Cloud Alibaba | 微服务基础设施（Nacos + Gateway） |
| MyBatis-Plus | 数据层 ORM，简化 CRUD |
| MySQL | 业务数据持久化 |
| Redis | 客服在线状态、会话缓存、Token 存储 |
| WebSocket | 实时双向通信 |
| JWT | 无状态登录鉴权 |
| Docker Compose | 开发/生产环境容器化部署 |

### 前端技术栈
- Vue CLI
- WebSocket 客户端 API
- Element UI / 原生组件

### 部署架构
用户 / 客服浏览器
↓
Nginx (前端静态资源 + 反向代理)
↓
Spring Cloud Gateway (统一入口)
↓
Nacos (服务注册与配置中心)
↓
[用户模块] [客服模块] [对话模块] [机器人模块]
↓
MySQL + Redis + RabbitMQ


---

## 四、核心功能实现

### 1. WebSocket 实时通信
- 客服端使用 `@ServerEndpoint` 建立长连接，登录后自动注册到 Redis `cs:online` 集合
- 用户进线时，系统从 Redis 查询在线客服列表，按策略分配
- 消息通过 WebSocket 推送到对方，同时异步写入 MySQL 持久化
- 心跳检测机制：服务端定时检测连接存活，断开后清理 Redis 状态并重新分配用户

### 2. 客服分配策略
- Redis Set 存储当前在线客服 ID（`cs:online`）
- 用户发起人工咨询时，轮询/随机选取在线客服
- 若客服中途下线，系统自动将该用户会话转移至其他在线客服或机器人

### 3. JWT + ThreadLocal 身份识别
- 登录成功后下发 JWT Token，后续 WebSocket 握手与 HTTP 请求携带 Token
- 服务端解析 JWT 获取用户/客服账号信息
- 使用 ThreadLocal 保存当前消息发送方 ID，用于会话路由与日志记录
- Token 过期或异常时，自动断开 WebSocket 连接并提示重新登录

### 4. 数据层设计
- MySQL 核心表：`user`（用户）、`cs_agent`（客服）、`dialogue`（对话）、`message`（消息）
- MyBatis-Plus 全局配置逻辑删除字段，支持分页查询与多条件检索
- 对话记录按时间倒序分页，支持客服查看历史消息
