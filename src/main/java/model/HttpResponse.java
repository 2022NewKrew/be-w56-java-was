package model;

import java.nio.charset.StandardCharsets;

public class HttpResponse {

    private static final String LINE_BREAK = "\r\n";
    private final StatusLine statusLine;
    private final Header header;

    public HttpResponse(StatusLine statusLine, Header header) {
        this.statusLine = statusLine;
        this.header = header;
    }

    protected byte[] headerMessage() {
        StringBuilder message = new StringBuilder(statusLine.message());
        message.append(LINE_BREAK);
        for (String str : header.messageList()) {
            message.append(str).append(LINE_BREAK);
        }
        message.append(LINE_BREAK);
        return message.toString().getBytes(StandardCharsets.UTF_8);
    }

    public byte[] message() {
        return headerMessage();
    }
}
