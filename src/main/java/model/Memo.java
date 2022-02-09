package model;

public class Memo {
    private final long memoId;
    private final String time;
    private final String writer;
    private final String contents;

    public Memo(long memoId, String writer, String contents, String time) {
        this.memoId = memoId;
        this.writer = writer;
        this.contents = contents;
        this.time = time;
    }

    private Memo(Builder builder) {
        this.memoId = builder.memoId;
        this.writer = builder.writer;
        this.contents = builder.contents;
        this.time = builder.time;
    }

    public static Builder builder() {
        return new Builder();
    }

    public long getMemoId() { return memoId; }

    public String getWriter() { return writer; }

    public String getTime() { return time; }

    public String getContents() { return contents; }

    public static class Builder {
        private long memoId;
        private String time;
        private String writer;
        private String contents;

        public Builder memoId(long memoId) {
            this.memoId = memoId;
            return this;
        }

        public Builder time(String time) {
            this.time = time;
            return this;
        }

        public Builder writer(String writer) {
            this.writer = writer;
            return this;
        }

        public Builder contents(String content) {
            this.contents = content;
            return this;
        }

        public Memo build() {
            return new Memo(this);
        }
    }
}
