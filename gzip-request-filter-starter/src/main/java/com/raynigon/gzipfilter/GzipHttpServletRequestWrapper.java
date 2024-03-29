package com.raynigon.gzipfilter;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

public class GzipHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public GzipHttpServletRequestWrapper(HttpServletRequest source) {
        super(source);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new GzipServletInputStream(super.getInputStream());
    }
}