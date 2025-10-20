package com.lidaa.accounts.audit;


import brave.Tracing;
import brave.baggage.BaggageFields;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.boot.logging.LogLevel;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuditInterceptor implements HandlerInterceptor {

    private final Tracing tracing;
    private final String loggingLevel;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        // Log the request information.
        if (StringUtils.isNotBlank(request.getHeader(BaggageFields.TRACE_ID.name()))) {
            MDC.put(BaggageFields.TRACE_ID.name(), request.getHeader(BaggageFields.TRACE_ID.name()));
        } else {
            tracing.tracer().newTrace();
        }


        final Map<String, Object> httpLog = new LinkedHashMap<>();
        httpLog.put("Method", request.getMethod());
        httpLog.put("http-protocol", request.getProtocol());
        httpLog.put("Endpoint", request.getRequestURI());
        httpLog.put("URL", request.getRequestURL().toString());
        String remoteAddr = "";

        remoteAddr = request.getHeader("X-FORWARDED-FOR");
        if (remoteAddr == null || "".equals(remoteAddr)) {
            remoteAddr = request.getRemoteAddr();
        }

        httpLog.put("RequestIP", remoteAddr);
        log(httpLog.toString());
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView) throws Exception {
        final Map<String, Object> httpLog = new LinkedHashMap<>();
        httpLog.put("ResponseStatus", String.valueOf(response.getStatus()));
        log(httpLog.toString());
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * Callback after completion of request processing, that is, after rendering
     * the view. Will be called on any outcome of handler execution, thus allows
     * for proper resource cleanup.
     * <p>Note: Will only be called if this interceptor's {@code preHandle}
     * method has successfully completed and returned {@code true}!
     * <p>As with the {@code postHandle} method, the method will be invoked on each
     * interceptor in the chain in reverse order, so the first interceptor will be
     * the last to be invoked.
     * <p><strong>Note:</strong> special considerations apply for asynchronous
     * request processing. For more details see
     * AsyncHandlerInterceptor
     * <p>The default implementation is empty.
     *
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler the handler that started asynchronous
     * execution, for type and/or instance examination
     * @param ex any exception thrown on handler execution, if any; this does not
     * include exceptions that have been handled through an exception resolver
     * @throws Exception in case of errors
     */
    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    @Async
    void log(final String message) {
        final LogLevel level = LogLevel.valueOf(loggingLevel);
        switch (level) {
            case DEBUG -> log.debug(message);
            case TRACE -> log.trace(message);
            case WARN -> log.warn(message);
            case ERROR, FATAL -> log.error(message);
            default -> log.info(message);
        }
    }

}