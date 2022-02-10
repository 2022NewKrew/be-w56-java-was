package model.memo;

import model.user_account.UserAccountDTO;

import java.time.LocalDateTime;

public class MemoDTO {
    private final String writer;
    private final LocalDateTime date;
    private final String contents;

    public MemoDTO(Builder builder) {
        this.writer = builder.writer;
        this.date = builder.date;
        this.contents = builder.contents;
    }

    public String getWriter() {
        return writer;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getContents() {
        return contents;
    }

    public static class Builder{
        private String writer;
        private LocalDateTime date;
        private String contents;

        public Builder(){
            writer = "";
            date = LocalDateTime.now();
            contents = "";
        }

        public Builder setWriter(String writer) {
            this.writer = writer;

            return this;
        }

        public Builder setContents(String contents) {
            this.contents = contents;

            return this;
        }

        public MemoDTO build(){
            return new MemoDTO(this);
        }
    }
}
