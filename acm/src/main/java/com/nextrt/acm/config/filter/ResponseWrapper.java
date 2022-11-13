package com.nextrt.acm.config.filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;

public class ResponseWrapper extends HttpServletResponseWrapper {
    private ByteArrayOutputStream buffer = null;
    private ServletOutputStream out = null;
    private PrintWriter writer = null;

    public ResponseWrapper(HttpServletResponse resp) throws IOException {
        super(resp);
        buffer = new ByteArrayOutputStream();// 真正存储数据的流
        out = new WapperedOutputStream(buffer);
        writer = new PrintWriter(new OutputStreamWriter(buffer));
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return out;
    }

    @Override
    public PrintWriter getWriter() throws UnsupportedEncodingException {
        return writer;
    }

    @Override
    public void flushBuffer() throws IOException {
        if (out != null) {
            out.flush();
        }
        if (writer != null) {
            writer.flush();
        }
    }

    @Override
    public void reset() {
        buffer.reset();
    }

    public byte[] getResponseData() throws IOException {
        flushBuffer();
        return buffer.toByteArray();
    }

    public String getContent() throws IOException {
        flushBuffer();
        return buffer.toString();
    }

    byte[] getBytes() {
        if(null != writer) {
            writer.close();
            return buffer.toByteArray();
        }

        if(null != buffer) {
            try {
                buffer.flush();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        return buffer.toByteArray();
    }

    private static class WapperedOutputStream extends ServletOutputStream {
        private ByteArrayOutputStream bos = null;

        WapperedOutputStream(ByteArrayOutputStream stream) throws IOException {
            bos = stream;
        }

        @Override
        public void write(int b) throws IOException {
            bos.write(b);
        }

        @Override
        public void write(byte[] b) throws IOException {
            bos.write(b, 0, b.length);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            bos.write(b, off, len);
        }

        @Override
        public boolean isReady() {
            return false;
        }
        /**
         * 获取缓存在 PrintWriter 中的响应数据
         * @return
         */

        @Override
        public void setWriteListener(WriteListener writeListener) {

        }
    }
}