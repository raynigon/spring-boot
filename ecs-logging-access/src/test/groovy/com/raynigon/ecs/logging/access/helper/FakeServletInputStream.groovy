package com.raynigon.ecs.logging.access.helper

import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream

class FakeServletInputStream extends ServletInputStream {

    private final InputStream delegate

    FakeServletInputStream(byte[] content = new byte[0]) {
        this.delegate = new ByteArrayInputStream(content)
    }

    @Override
    int read() throws IOException {
        return delegate.read()
    }

    @Override
    boolean isFinished() {
        return delegate.available() == 0
    }

    @Override
    boolean isReady() {
        return true
    }

    @Override
    void setReadListener(ReadListener readListener) {
        // not needed for tests
    }
}
