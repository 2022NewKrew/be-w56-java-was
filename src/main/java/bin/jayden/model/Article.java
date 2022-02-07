package bin.jayden.model;

public class Article {
    private final long id;
    private final String title;
    private final long writerId;
    private final String writerName;
    private final String time;

    public Article(String title, long writerId) {
        this(0, title, writerId, null, null);
    }

    public Article(long id, String title, long writerId, String writerName, String time) {
        this.id = id;
        this.title = title;
        this.writerId = writerId;
        this.writerName = writerName;
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public long getWriterId() {
        return writerId;
    }

    public String getWriterName() {
        return writerName;
    }
}
