package com.beeplay.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebMvc
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        //js 原生版本 对应demo index.html
        registry.addHandler(systemWebSocketHandler(), "/webSocketServer").setAllowedOrigins("*").addInterceptors(new HandshakeInterceptor());
        //socket js版本 对应demo index1.html
        registry.addHandler(systemWebSocketHandler(), "/sockjs/webSocketServer").setAllowedOrigins("*").withSockJS().setHeartbeatTime(10000).setDisconnectDelay(1000);
    }

    @Bean
    public WebSocketHandler systemWebSocketHandler() {
        return new SystemWebSocketHandler();
    }
}