package pe.gob.minsa.bean;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import pe.gob.minsa.service.KafkaProducerService;

@Component
@Order(1)
public class AuditFilter implements Filter {

    private final KafkaProducerService kafkaProducerService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // Autowired implicitly
    public AuditFilter(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        Instant start = Instant.now();

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        String username = requestWrapper.getHeader("username").toString();
        String ipclient = requestWrapper.getHeader("x-forwarded-for");
        String clientURL = requestWrapper.getRequestURL().toString();
        String clientURI = requestWrapper.getRequestURI().toString();
        String getQuery = requestWrapper.getQueryString();
        String getMethod = requestWrapper.getMethod().toString();

        String requestStr = new String();
        String responseStr = new String();
        String time;

        try {
        	if (ipclient == null) {
                ipclient = requestWrapper.getRemoteAddr();
            }
        	
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            Instant finish = Instant.now();
            time = Duration.between(start, finish).toMillis() + "";

            byte[] requestArray = requestWrapper.getContentAsByteArray();
            byte[] responseArray = responseWrapper.getContentAsByteArray();
            requestStr = new String(requestArray, requestWrapper.getCharacterEncoding());
            responseStr = new String(responseArray, responseWrapper.getCharacterEncoding());
            responseWrapper.copyBodyToResponse();

        }

        try {
        	
            kafkaProducerService.createAuditoriaDatos(requestStr, getMethod, getQuery,
                    responseStr, username, ipclient,
                    clientURL, clientURI, time);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        return;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub

    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

}