package model;

import java.time.LocalDate;

public class Memo {
    private long id;
    private User user;
    private String content;
    private LocalDate createDate;

    public Memo(long id, User user, String content, LocalDate createDate) {
        this.id = id;
        this.user = user;
        this.content = content;
        this.createDate = createDate;
    }

    public Memo(User user, String content) {
        this.user = user;
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public String getContent() {
        return content;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }
}
