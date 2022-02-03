package com.kakao.http.header;

public class ContentTypeHeader extends AbstractHttpHeader {
    private static final String KEY = "Content-Type";

    private final String value;

    public ContentTypeHeader(String fileName) {
        String fileExtension = getFileExtension(fileName);
        this.value = ContentTypeMap.findByExtension(fileExtension);
    }

    private String getFileExtension(String fileName) {
        String[] fileNameTokens = fileName.split("\\.");
        if (fileNameTokens.length == 1) {
            return "";
        }
        return fileNameTokens[fileNameTokens.length - 1];
    }

    @Override
    public String key() {
        return KEY;
    }

    @Override
    public String value() {
        return this.value;
    }
}
