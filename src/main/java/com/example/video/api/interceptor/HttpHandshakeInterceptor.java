package com.example.video.api.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
public class HttpHandshakeInterceptor implements HandshakeInterceptor {
    /*
        Interceptor for WebSocket handshake requests.
        Can be used to inspect the handshake request and response as well as to pass attributes to the target WebSocketHandler.
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            HttpSession session = ((ServletServerHttpRequest) request).getServletRequest().getSession();
            session.setMaxInactiveInterval(1800); // HttpSession will expire in 30 minutes

            // HTTP Session 저장
            attributes.put("httpSession", session);

            System.out.println();

            log.info("{} HttpSession {}", session.isNew() ? "New" : "Old", session.getId());
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        int i = 0;
    }
}