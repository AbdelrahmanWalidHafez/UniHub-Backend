package com.unihub.auth.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;



@Slf4j
@Aspect
@Component
public class LoggerAspect {

    @AfterThrowing(
            pointcut = """
            within(com.unihub.auth..*) &&
            !within(com.unihub.auth.security.filter.*) &&
            !@within(org.springframework.context.annotation.Configuration) &&
            !@within(org.springframework.boot.context.properties.ConfigurationProperties)
        """,
            throwing = "exception"
    )
    public void logException(JoinPoint joinPoint, Exception exception) {
        log.error(
                "{} - An exception happened due to: {}",
                joinPoint.getSignature(),
                exception.getMessage(),
                exception
        );
    }
}