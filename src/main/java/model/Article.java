package model;

public class Article {
    private String title;
    private String dateTime;
    private String writer;

    public Article(String title, String dateTime, String writer) {
        this.title = title;
        this.dateTime = dateTime;
        this.writer = writer;
    }

    public String getTitle() {
        return title;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getWriter() {
        return writer;
    }
}
