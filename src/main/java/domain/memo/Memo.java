package domain.memo;

import java.time.LocalDateTime;

public class Memo {

    private final int id;
    private final String writer;
    private final String content;
    private final LocalDateTime createdAt;

    public Memo(int id, String writer, String content, LocalDateTime createdAt) {
        this.id = id;
        this.writer = writer;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public int getId() {
        return id;
    }

    public String getWriter() {
        return writer;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public static class Builder {
        private int id;
        private String writer;
        private String content;
        private LocalDateTime createdAt;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder writer(String writer) {
            this.writer = writer;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Memo build() {
            return new Memo(id, writer, content, createdAt);
        }
    }
}
