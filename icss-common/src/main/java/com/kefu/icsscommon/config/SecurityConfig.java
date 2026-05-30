package com.kefu.icsscommon.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public KeyPair keyPair(JwtProperties properties) throws Exception {
        // JDK 21 默认 PKCS12 格式
        KeyStore keyStore = KeyStore.getInstance("pkcs12");
        keyStore.load(
                properties.getLocation().getInputStream(),
                properties.getPassword().toCharArray()
        );

        PrivateKey privateKey = (PrivateKey) keyStore.getKey(
                properties.getAlias(),
                properties.getPassword().toCharArray()
        );

        Certificate certificate = keyStore.getCertificate(properties.getAlias());
        PublicKey publicKey = certificate.getPublicKey();

        return new KeyPair(publicKey, privateKey);
    }
}