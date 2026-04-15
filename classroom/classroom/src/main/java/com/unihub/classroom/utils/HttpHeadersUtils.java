package com.unihub.classroom.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class HttpHeadersUtils {

    public UUID fetchUidFromHeader(HttpServletRequest request){
        return UUID.fromString(request.getHeader("X-User-University-Id"));
    }

    public UUID fetchCidFromHeader(HttpServletRequest request){
        return UUID.fromString(request.getHeader("X-User-College-Id"));
    }

    public String fetchEmailFromHeader(HttpServletRequest request){
        return request.getHeader("X-User-Email");
    }

}
