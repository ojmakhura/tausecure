package bw.co.roguesystems.tau.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import bw.co.roguesystems.tau.filter.UserAgentValidationFilter;


/**
 * Configuration class for filters.
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<UserAgentValidationFilter> userAgentFilterRegistration(
            UserAgentValidationFilter filter) {

        FilterRegistrationBean<UserAgentValidationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/feedback"); // Apply to all endpoints
        registrationBean.setName("UserAgentValidationFilter");
        registrationBean.setOrder(1); // Specify order if you have multiple filters
        return registrationBean;
    }
}
