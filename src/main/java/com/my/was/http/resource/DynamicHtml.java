package com.my.was.http.resource;

import java.nio.charset.StandardCharsets;

public class DynamicHtml implements Resource {

    private String html;

    public DynamicHtml(String html) {
        this.html = html;
    }

    @Override
    public String getType() {
        return "text/html";
    }

    @Override
    public byte[] getContent() {
        return html.getBytes(StandardCharsets.UTF_8);
    }
}
