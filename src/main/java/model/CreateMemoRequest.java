package model;

import java.util.Map;

public class CreateMemoRequest {

    private final String writer;
    private final String content;

    public CreateMemoRequest(String writer, String content) {
        this.writer = writer;
        this.content = content;
    }

    public static CreateMemoRequest from(String writer, Map<String, String> queryData) {
        String content = queryData.get("content");
        return new CreateMemoRequest(writer, content);
    }

    public String getWriter() {
        return writer;
    }

    public String getContent() {
        return content;
    }
}
