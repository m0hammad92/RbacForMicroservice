package com.lidaa.accounts.audit;

import brave.Tracing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.boot.actuate.audit.InMemoryAuditEventRepository;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.boot.logging.LogLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The Class AuditConfig to register audit events.
 */
@Configuration
@Slf4j
/**
 * Instantiates a new audit config.
 *
 * @param tracing the tracing
 */
@RequiredArgsConstructor
public class AuditConfig {

    /**
     * The tracing.
     */
    private final Tracing tracing;

    /**
     * The logging level.
     */
    @Value("${logging.level.com.lidaa.accounts}")
    private String loggingLevel;

    /**
     * Add Spring MVC lifecycle interceptors for pre- and post-processing of
     * controller method invocations and resource handler requests.
     * Interceptors can be registered to apply to all requests or be limited
     * to a subset of URL patterns.
     *
     * @return new AuditInterceptor
     */
    @Bean
    public AuditInterceptor auditInterceptor() {
        return new AuditInterceptor(tracing, loggingLevel);
    }

    /**
     * Bean to enable /auditevents actuator endpoint and logging security audit event
     *
     * @return the audit event repository
     */
    @Bean
    public AuditEventRepository repository() {
        return new InMemoryAuditEventRepository();
    }

    /**
     * Web mvc configurer.
     *
     * @return the web mvc configurer
     */
    @Bean
    WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            /**
             * Add Spring MVC lifecycle interceptors for pre- and post-processing of
             * controller method invocations and resource handler requests. Interceptors can
             * be registered to apply to all requests or be limited to a subset of URL
             * patterns.
             *
             * @param registry
             */
            @Override
            public void addInterceptors(final InterceptorRegistry registry) {
                registry.addInterceptor(new AuditInterceptor(tracing, loggingLevel));

                WebMvcConfigurer.super.addInterceptors(registry);
            }

            @Override
            public void addCorsMappings(final CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST", "PUT", "DELETE");
            }
        };
    }


    /**
     * Method to listen Security Audit event.
     * /auditevents actuator endpoint needs to be enabled for @EventListener annotated method to listen the security audit events
     *
     * @param event the event
     */
    @EventListener
    @Async
    public void on(final AuditApplicationEvent event) {

        final Map<String, Object> eventLog = new LinkedHashMap<>();
        eventLog.put("event.type", event.getAuditEvent().getType());
        eventLog.put("event.principal", event.getAuditEvent().getPrincipal());
        eventLog.put("event.data", event.getAuditEvent().getData());
        String message = "Security Auditing Event Received: " + eventLog.toString();
        log(message);
    }

    @Async
    void log(final String message) {
        final LogLevel level = LogLevel.valueOf(loggingLevel);
        switch (level) {
            case INFO -> log.info(message);
            case DEBUG -> log.debug(message);
            case TRACE -> log.trace(message);
            case WARN -> log.warn(message);
            case ERROR, FATAL -> log.error(message);
            default -> log.info(message);
        }
    }
}