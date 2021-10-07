package com.nic.billingstats.filter;

import com.google.common.base.CharMatcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@WebFilter(urlPatterns = "/billingstats/*")
class AuthenticationFilter implements Filter {

    static final String BEARER_KEYWORD = "bearer";

    static final Set<String> excludeURLPathList = new HashSet<>(Arrays.asList("/manage/health"));

    @Value("${authorization_key}")
    private String authorizationKey;

    @Override
    public void init(FilterConfig filterConfig) {
        // initialization
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.debug("Inside AuthenticationFilter ...");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authHeader = httpRequest.getHeader("authorization");
        /**
         * Incase of request going through proxy (eg: load balancer)
         * look for originating client IP in X-Forwarded-For header
         * Else retrieve the client IP  via getRemoteAddr()
         * */
        String remoteAddress = httpRequest.getHeader("X-Forwarded-For");
        String remoteClientAddress = StringUtils.isNotBlank(remoteAddress) ? remoteAddress : httpRequest.getRemoteAddr();
        String remoteUserAgent = httpRequest.getHeader("User-Agent");
        String urlPath = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        if (excludeURLPathList.contains(urlPath)) {
            log.debug("health check");
            chain.doFilter(request, response);
        } else if (authorize(authHeader)) {
            log.info("Authorization SUCCESS. RemoteClientAddress : {}, User-Agent: {}", remoteClientAddress, remoteUserAgent);
            chain.doFilter(request, response);
        } else {
            authenticationFailureResponse(remoteClientAddress, remoteUserAgent, authHeader, response);
        }
    }

    @Override
    public void destroy() {
        // destroy
    }

    static void authenticationFailureResponse(String remoteClientAddress, String remoteUserAgent, String authHeader, ServletResponse response) throws IOException {
        log.info("Authorization FAILURE. RemoteClientAddress : {}, User-Agent: {}, Authorization Header : {}", remoteClientAddress, remoteUserAgent, authHeader);
        ((HttpServletResponse) response).setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getOutputStream().write("Invalid Authorization Token".getBytes());
    }

    boolean authorize(String authHeader) {
        if (StringUtils.isNotBlank(authHeader)) {
            try {
                String encodedToken = extractBearerToken(authHeader);
                String token = decodeToken(encodedToken);
                return token.equals(authorizationKey);
            } catch (Exception e) {
                log.error("AuthenticationFilter.authorize Exception while authorizing the token.{}.{}", e.getMessage(), e.getStackTrace());
                return false;
            }
        }
        return false;
    }

    static String extractBearerToken(String authHeader) {
        if (authHeader.toLowerCase().startsWith(BEARER_KEYWORD)) {
            return authHeader.substring(BEARER_KEYWORD.length()).trim();
        }
        return "";
    }

    static String decodeToken(String encodedToken) {
        byte[] decoded = Base64.getDecoder().decode(CharMatcher.whitespace().removeFrom(encodedToken));
        return new String(decoded);
    }

}