package dto.memo;

import model.Memo;

public class MemoItemDto {
    private String writerName;
    private String content;
    private String createdAt;

    public MemoItemDto() {
    }

    public MemoItemDto(String writerName, String content, String createdAt) {
        this.writerName = writerName;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static MemoItemDto of(Memo memo) {
        return new MemoItemDto(memo.getWriter().getName(), memo.getContent(), memo.getCreatedAt().toString());
    }

    public String getWriterName() {
        return writerName;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
