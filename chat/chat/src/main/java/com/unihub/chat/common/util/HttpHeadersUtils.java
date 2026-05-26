package com.unihub.chat.common.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class HttpHeadersUtils {

    private static final String EMAIL_HEADER = "X-User-Email";
    private static final String UNIVERSITY_HEADER = "X-User-University-Id";
    private static final String COLLEGE_HEADER = "X-User-College-Id";

    public String extractEmail(HttpServletRequest request) {
        return request.getHeader(EMAIL_HEADER);
    }

    public UUID extractTid(HttpServletRequest request) {
        String tid = request.getHeader(UNIVERSITY_HEADER);
        return tid != null ? UUID.fromString(tid) : null;
    }

    public UUID extractCid(HttpServletRequest request) {
        String cid = request.getHeader(COLLEGE_HEADER);
        return cid != null ? UUID.fromString(cid) : null;
    }
}