package com.unihub.universitymanagement.universitymanagement.common.audit;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditAwareImpl")
@RequiredArgsConstructor
public class AuditAwareImpl implements AuditorAware<String> {

    private final HttpServletRequest request;

    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            String email = request.getHeader("X-User-Email");
            return Optional.of(email != null ? email : "System");
        } catch (IllegalStateException e) {
            return Optional.of("System");
        }
    }
}
