package com.learning.demo.model;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author yuehewei <yuehewei@kuaishou.com>
 * Created on 2023-07-11
 */
public class MyCustomMultipartFile implements MultipartFile {
    private final String name;
    private final InputStream inputStream;

    public MyCustomMultipartFile(String name, InputStream inputStream) {
        this.name = name;
        this.inputStream = inputStream;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOriginalFilename() {
        return name;
    }

    @Override
    public String getContentType() {
        return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public long getSize() {
        try {
            return inputStream.available();
        } catch (IOException e) {
            return 0;
        }
    }

    @Override
    public byte[] getBytes() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return inputStream;
    }

    @Override
    public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
        throw new UnsupportedOperationException();
    }

}
