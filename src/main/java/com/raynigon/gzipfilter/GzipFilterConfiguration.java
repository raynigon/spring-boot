package com.raynigon.gzipfilter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GzipFilterConfiguration {

    @Bean
    public FilterRegistrationBean<GzipRequestFilter> gzipBodyDecompressFilterRegistration() {
        FilterRegistrationBean<GzipRequestFilter> registration = new FilterRegistrationBean<>();
        registration.setName("gzipBodyDecompressFilter");
        registration.setFilter(new GzipRequestFilter());
        registration.setOrder(10);
        return registration;
    }
}
