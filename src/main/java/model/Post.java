package model;

public class Post {
    private static Long autoIncrease = 0L;
    private Long id;
    private String author;
    private String content;
    private String postTime;

    public Post(String author, String content, String postTime) {
        this.id = autoIncrease++;
        this.author = author;
        this.content = content;
        this.postTime = postTime;
    }

    public Long getId() {
        return id;
    }

    public String getPostTime() {
        return postTime;
    }
}
