package com.unihub.chat.ws.interceptor;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class UserHandshakeInterceptor implements HandshakeInterceptor {

    private static final String EMAIL_HEADER = "X-User-Email";
    private static final String UNIVERSITY_HEADER = "X-User-University-Id";
    private static final String COLLEGE_HEADER = "X-User-College-Id";

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String email = request.getHeaders().getFirst(EMAIL_HEADER);
        String tid = request.getHeaders().getFirst(UNIVERSITY_HEADER);
        String cid = request.getHeaders().getFirst(COLLEGE_HEADER);

        if (email != null && !email.isBlank()) {
            attributes.put("email", email);
        }
        if (tid != null && !tid.isBlank()) {
            attributes.put("tid", tid);
        }
        if (cid != null && !cid.isBlank()) {
            attributes.put("cid", cid);
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                WebSocketHandler wsHandler, Exception exception) {
    }
}