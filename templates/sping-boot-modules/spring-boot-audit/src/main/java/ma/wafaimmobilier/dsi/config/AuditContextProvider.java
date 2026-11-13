package ma.wafaimmobilier.dsi.config;


import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import ma.wafaimmobilier.dsi.models.AuditContext;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@AllArgsConstructor
public class AuditContextProvider {

    private final String applicationName;
    private static final String DEFAULT_ACTOR = "SYSTEM";
    private final Tracer tracer;

    public AuditContext getCurrentContext() {
        return new AuditContext(
                getCurrentActor(),
                applicationName,
                getCurrentTraceId()
        );
    }

    private String getCurrentActor() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return DEFAULT_ACTOR;
        }

        return authentication.getName();
    }

    private String getCurrentTraceId() {
        Span currentSpan = tracer.currentSpan();

        return currentSpan != null
                ? currentSpan.context().traceId()
                : null;
    }
}