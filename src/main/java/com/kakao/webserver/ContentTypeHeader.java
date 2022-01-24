package com.kakao.webserver;

public class ContentTypeHeader extends AbstractHttpHeader {
    private static final String KEY = "Content-Type";
    private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
    private static final String DEFAULT_CHARSET = "; charset=utf-8";

    private final String value;

    public ContentTypeHeader(String fileName) {
        String fileExtension = getFileExtension(fileName);
        switch (fileExtension) {
            case "html":
            case "js":
                this.value = "text/html" + DEFAULT_CHARSET;
                break;
            case "css":
                this.value = "text/css" + DEFAULT_CHARSET;
                break;
            default:
                this.value = DEFAULT_CONTENT_TYPE;
                break;
        }
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
