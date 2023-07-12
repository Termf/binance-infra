package com.binance.platform.openfeign.body;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.util.WebUtils;

public class CustomServletResponseWrapper extends HttpServletResponseWrapper {

    private final FastByteArrayOutputStream content = new FastByteArrayOutputStream(1024);

    private ServletOutputStream outputStream;

    private PrintWriter writer;

    private int statusCode = HttpServletResponse.SC_OK;

    private Integer contentLength;

    private boolean isGzip = false;

    public CustomServletResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public void setStatus(int sc) {
        super.setStatus(sc);
        this.statusCode = sc;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setStatus(int sc, String sm) {
        super.setStatus(sc, sm);
        this.statusCode = sc;
    }

    @Override
    public void sendError(int sc) throws IOException {
        copyBodyToResponse(false);
        try {
            super.sendError(sc);
        } catch (IllegalStateException ex) {
            super.setStatus(sc);
        }
        this.statusCode = sc;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void sendError(int sc, String msg) throws IOException {
        copyBodyToResponse(false);
        try {
            super.sendError(sc, msg);
        } catch (IllegalStateException ex) {
            super.setStatus(sc, msg);
        }
        this.statusCode = sc;
    }

    @Override
    public void sendRedirect(String location) throws IOException {
        copyBodyToResponse(false);
        super.sendRedirect(location);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (this.outputStream == null) {
            this.outputStream = new ResponseServletOutputStream(getResponse().getOutputStream());
        }
        return this.outputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (this.writer == null) {
            String characterEncoding = getCharacterEncoding();
            this.writer = (characterEncoding != null ? new ResponsePrintWriter(characterEncoding)
                : new ResponsePrintWriter(WebUtils.DEFAULT_CHARACTER_ENCODING));
        }
        return this.writer;
    }

    @Override
    public void flushBuffer() throws IOException {}

    @Override
    public void setContentLength(int len) {
        if (len > this.content.size()) {
            this.content.resize(len);
        }
        this.contentLength = len;
    }

    public void setContentLengthLong(long len) {
        if (len > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(
                "Content-Length exceeds ContentCachingResponseWrapper's maximum (" + Integer.MAX_VALUE + "): " + len);
        }
        int lenInt = (int)len;
        if (lenInt > this.content.size()) {
            this.content.resize(lenInt);
        }
        this.contentLength = lenInt;
    }

    @Override
    public void setBufferSize(int size) {
        if (size > this.content.size()) {
            this.content.resize(size);
        }
    }

    @Override
    public void resetBuffer() {
        this.content.reset();
    }

    @Override
    public void reset() {
        super.reset();
        this.content.reset();
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public byte[] getContentAsByteArray() {
        return this.content.toByteArray();
    }

    public InputStream getContentInputStream() {
        return this.content.getInputStream();
    }

    public int getContentSize() {
        return this.content.size();
    }

    public void copyBodyToResponse() throws IOException {
        copyBodyToResponse(true);
    }

    protected void copyBodyToResponse(boolean complete) throws IOException {
        if (this.isGzip) {
            if (this.content.size() > 0) {
                HttpServletResponse rawResponse = (HttpServletResponse)getResponse();
                GZIPOutputStream gZipOutputStream = new GZIPOutputStream(rawResponse.getOutputStream());
                this.content.writeTo(gZipOutputStream);
                gZipOutputStream.finish();
                gZipOutputStream.close();
                this.content.reset();
                if (complete) {
                    super.flushBuffer();
                }
            }
        } else {
            if (this.content.size() > 0) {
                HttpServletResponse rawResponse = (HttpServletResponse)getResponse();
                if ((complete || this.contentLength != null) && !rawResponse.isCommitted()) {
                    rawResponse.setContentLength(complete ? this.content.size() : this.contentLength);
                    this.contentLength = null;
                }
                this.content.writeTo(rawResponse.getOutputStream());
                this.content.reset();
                if (complete) {
                    super.flushBuffer();
                }
            }
        }
    }

    public void setIsGzip(boolean isGzip) {
        this.isGzip = isGzip;
    }

    private class ResponseServletOutputStream extends ServletOutputStream {

        private final ServletOutputStream os;

        public ResponseServletOutputStream(ServletOutputStream os) {
            this.os = os;
        }

        @Override
        public void write(int b) throws IOException {
            content.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            content.write(b, off, len);
        }

        @Override
        public boolean isReady() {
            return this.os.isReady();
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
            this.os.setWriteListener(writeListener);
        }
    }

    private class ResponsePrintWriter extends PrintWriter {

        public ResponsePrintWriter(String characterEncoding) throws UnsupportedEncodingException {
            super(new OutputStreamWriter(content, characterEncoding));
        }

        @Override
        public void write(char[] buf, int off, int len) {
            super.write(buf, off, len);
            super.flush();
        }

        @Override
        public void write(String s, int off, int len) {
            super.write(s, off, len);
            super.flush();
        }

        @Override
        public void write(int c) {
            super.write(c);
            super.flush();
        }
    }

}
