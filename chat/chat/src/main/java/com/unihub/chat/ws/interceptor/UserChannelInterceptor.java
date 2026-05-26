package com.unihub.chat.ws.interceptor;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Map;
import java.util.Objects;

@Component
public class UserChannelInterceptor implements ChannelInterceptor {

    private static final String EMAIL_HEADER = "X-User-Email";
    private static final String UNIVERSITY_HEADER = "X-User-University-Id";
    private static final String COLLEGE_HEADER = "X-User-College-Id";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null || !StompCommand.CONNECT.equals(accessor.getCommand())) {
            return message;
        }

        String email = firstNonBlank(
                accessor.getFirstNativeHeader(EMAIL_HEADER),
                sessionAttr(accessor, "email")
        );
        String tid = firstNonBlank(
                accessor.getFirstNativeHeader(UNIVERSITY_HEADER),
                sessionAttr(accessor, "tid")
        );
        String cid = firstNonBlank(
                accessor.getFirstNativeHeader(COLLEGE_HEADER),
                sessionAttr(accessor, "cid")
        );

        if (email == null || email.isBlank()) {
            throw new IllegalStateException("Missing X-User-Email STOMP header");
        }

        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
        if (sessionAttributes != null) {
            sessionAttributes.put("email", email);
            sessionAttributes.put("tid", tid != null ? tid : "");
            sessionAttributes.put("cid", cid != null ? cid : "");
        }

        accessor.setUser(new StompPrincipal(email));
        return message;
    }

    private String firstNonBlank(String... values) {
        if (values == null) {
            return null;
        }
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }

    private String sessionAttr(StompHeaderAccessor accessor, String key) {
        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
        if (sessionAttributes == null) {
            return null;
        }
        Object value = sessionAttributes.get(key);
        return value != null ? Objects.toString(value, null) : null;
    }

    public record StompPrincipal(String name) implements Principal {
        @Override
        public String getName() {
            return name;
        }
    }
}