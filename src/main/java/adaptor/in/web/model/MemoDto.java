package adaptor.in.web.model;

public class MemoDto {

    private final String writer;
    private final String content;

    public MemoDto(String writer, String content) {
        this.writer = writer;
        this.content = content;
    }

    public String getWriter() {
        return writer;
    }

    public String getContent() {
        return content;
    }

}
