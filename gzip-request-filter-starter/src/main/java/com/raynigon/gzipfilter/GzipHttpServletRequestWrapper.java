package com.raynigon.gzipfilter;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
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