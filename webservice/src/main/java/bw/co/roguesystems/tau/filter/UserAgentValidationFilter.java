package bw.co.roguesystems.tau.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter that blocks requests from certain User-Agents.
 */
@Component
public class UserAgentValidationFilter implements Filter {

    private static final List<String> BLOCKED_USER_AGENTS = List.of(
            "curl", "wget", "PostmanRuntime", "bot", "crawler", "spider", "scrapy", "httpclient");

    @Override
    public void doFilter(jakarta.servlet.ServletRequest request, jakarta.servlet.ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest httpRequest) {
            String userAgent = httpRequest.getHeader("User-Agent");
            if (userAgent == null || isBlockedUserAgent(userAgent)) {
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Blocked User-Agent");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean isBlockedUserAgent(String userAgent) {
        return BLOCKED_USER_AGENTS.stream().anyMatch(
                blockedUserAgent -> userAgent.toLowerCase().contains(blockedUserAgent.toLowerCase()));
    }
}
