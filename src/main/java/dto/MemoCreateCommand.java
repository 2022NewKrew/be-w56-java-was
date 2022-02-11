package dto;

public class MemoCreateCommand {
    private final String writer;
    private final String contents;

    private MemoCreateCommand(Builder builder) {
        this.writer = builder.writer;
        this.contents = builder.contents;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getWriter() { return writer; }

    public String getContents() { return contents; }

    public static class Builder {
        private String writer;
        private String contents;

        public Builder writer(String writer) {
            this.writer = writer;
            return this;
        }

        public Builder contents(String content) {
            this.contents = content;
            return this;
        }

        public MemoCreateCommand build() {
            return new MemoCreateCommand(this);
        }
    }
}
