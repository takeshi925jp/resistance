package com.example.resistance.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 受信エンドポイントの設定
        config.enableSimpleBroker("/receive");
	// 送信エンドポイントの設定（プレフィクス定義）
        config.setApplicationDestinationPrefixes("/send");

    }

//	@Override
//	public void registerStompEndpoints(StompEndpointRegistry registry) {
//		// 初回WebSocket通信開始時のエンドポイントの設定
//		registry.addEndpoint("/websocket").withSockJS();
//	}

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 初回WebSocket通信開始時のエンドポイントの設定
        registry.addEndpoint("/websocket").setAllowedOrigins("*").withSockJS();
    }

//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/simulation").setAllowedOrigins("*").withSockJS();
//    }

}
