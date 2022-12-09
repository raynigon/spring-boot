package com.raynigon.gzipfilter;


import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class GzipServletInputStream extends ServletInputStream {

    private final ServletInputStream source;
    private final GZIPInputStream gzipStream;

    public GzipServletInputStream(ServletInputStream source) throws IOException {
        this.source = source;
        gzipStream = new GZIPInputStream(source);
    }

    @Override
    public boolean isFinished() {
        try {
            return gzipStream.available() <= 0;
        } catch (IOException e) {
            return true;
        }
    }

    @Override
    public boolean isReady() {
        return source.isReady();
    }

    @Override
    public void setReadListener(ReadListener listener) {
        source.setReadListener(listener);
    }

    @Override
    public int read() throws IOException {
        return gzipStream.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return gzipStream.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return gzipStream.read(b, off, len);
    }

    @Override
    public byte[] readAllBytes() throws IOException {
        return gzipStream.readAllBytes();
    }

    @Override
    public byte[] readNBytes(int len) throws IOException {
        return gzipStream.readNBytes(len);
    }

    @Override
    public int readNBytes(byte[] b, int off, int len) throws IOException {
        return gzipStream.readNBytes(b, off, len);
    }
}
