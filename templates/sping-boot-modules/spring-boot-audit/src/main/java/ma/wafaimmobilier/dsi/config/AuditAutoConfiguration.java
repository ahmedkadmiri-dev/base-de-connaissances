package ma.wafaimmobilier.dsi.config;


import ma.wafaimmobilier.dsi.services.AuditService;
import ma.wafaimmobilier.dsi.services.impl.AuditServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import(AuditDataSourceConfiguration.class)
public class AuditAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AuditService auditService(
            AuditServiceImpl auditServiceImpl) {

        return auditServiceImpl;
    }
}