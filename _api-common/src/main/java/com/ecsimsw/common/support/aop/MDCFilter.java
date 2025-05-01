package com.ecsimsw.common.support.aop;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

import static com.ecsimsw.common.config.LogConfig.TRACE_ID;
import static com.ecsimsw.common.config.LogConfig.TRACE_ID_HEADER;

@Component
public class MDCFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            MDC.put("threadId", String.valueOf(Thread.currentThread().getId()));
            var httpRequest = (HttpServletRequest) request;
            var traceId = httpRequest.getHeader(TRACE_ID_HEADER);
            if(traceId != null) {
                MDC.put(TRACE_ID, traceId);
                chain.doFilter(request, response);
            } else {
                var newTraceId = UUID.randomUUID().toString();
                MDC.put(TRACE_ID, newTraceId);
                chain.doFilter(request, response);
            }
        } finally {
            MDC.clear();
        }
    }
}
