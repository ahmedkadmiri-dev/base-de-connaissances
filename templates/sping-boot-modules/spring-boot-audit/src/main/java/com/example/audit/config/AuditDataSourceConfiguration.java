
package com.example.audit.config;

import com.example.audit.entities.AuditEventEntity;
import com.example.audit.repositories.AuditEventRepository;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(AuditProperties.class)
@EnableJpaRepositories(
        basePackageClasses = AuditEventRepository.class,
        entityManagerFactoryRef = "auditEntityManagerFactory",
        transactionManagerRef = "auditTransactionManager"
)
@ConditionalOnProperty(
        prefix = "audit.datasource",
        name = "url"
)
public class AuditDataSourceConfiguration {

    @Bean(name = "auditDataSource", destroyMethod = "close")
    public DataSource auditDataSource(AuditProperties properties) {

        HikariDataSource dataSource = new HikariDataSource();

        dataSource.setJdbcUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());

        return dataSource;
    }

    @Bean(name = "auditEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean auditEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("auditDataSource") DataSource auditDataSource) {

        return builder
                .dataSource(auditDataSource)
                .packages(AuditEventEntity.class)
                .persistenceUnit("audit")
                .build();
    }

    @Bean(name = "auditTransactionManager")
    public JpaTransactionManager auditTransactionManager(
            @Qualifier("auditEntityManagerFactory")
            EntityManagerFactory auditEntityManagerFactory) {

        return new JpaTransactionManager(auditEntityManagerFactory);
    }
}
