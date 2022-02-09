package model;

import java.time.LocalDateTime;

/**
 * Created by melodist
 * Date: 2022-02-08 008
 * Time: 오후 7:47
 */
public class Article {

    private User user;
    private LocalDateTime createdDate;
    private String content;

    public Article(User user, LocalDateTime createdDate, String content) {
        this.user = user;
        this.createdDate = createdDate;
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public String getContent() {
        return content;
    }
}
