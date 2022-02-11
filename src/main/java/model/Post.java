package model;

import java.time.LocalDate;

public class Post {
    private int postId;
    private LocalDate date;
    private String writer;
    private String content;

    public Post(int postId, LocalDate date, String writer, String content) {
        this.postId = postId;
        this.date = date;
        this.writer = writer;
        this.content = content;
    }

    public int getPostId() {
        return postId;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getWriter() {
        return writer;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId='" + postId + '\'' +
                ", date=" + date +
                ", writer='" + writer + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
