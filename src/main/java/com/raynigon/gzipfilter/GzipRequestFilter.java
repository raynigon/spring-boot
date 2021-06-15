package com.raynigon.gzipfilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

class GzipRequestFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(GzipRequestFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (!isGzipped(request)) {
            logger.trace(
                    "Skip Request content encoding: {} is not gzip",
                    request.getHeader(HttpHeaders.CONTENT_ENCODING)
            );
            chain.doFilter(request, response);
            return;
        }

        if (!isJsonContent(request)) {
            logger.trace(
                    "Skip Request content encoding: {} is not json",
                    request.getHeader(HttpHeaders.CONTENT_TYPE)
            );
            chain.doFilter(request, response);
            return;
        }

        logger.trace("Request will be decompressed with Gzip");
        GzipHttpServletRequestWrapper wrappedRequest = new GzipHttpServletRequestWrapper(request);
        chain.doFilter(wrappedRequest, response);
    }


    private boolean isJsonContent(HttpServletRequest request) {
        String contentTypeHeader = request.getHeader(HttpHeaders.CONTENT_TYPE);
        if (contentTypeHeader == null) return false;
        return contentTypeHeader.toLowerCase()
                .startsWith(APPLICATION_JSON_VALUE.toLowerCase());
    }

    private boolean isGzipped(HttpServletRequest request) {
        String encodingHeader = request.getHeader(HttpHeaders.CONTENT_ENCODING);
        if (encodingHeader == null) return false;
        return Arrays.stream(encodingHeader.split(","))
                .map(String::trim)
                .anyMatch(it -> it.equals("gzip"));
    }
}