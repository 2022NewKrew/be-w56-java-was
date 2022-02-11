package application.domain;

import java.time.LocalDate;

public class Memo {

    private String userId;
    private String content;
    private LocalDate date;

    public Memo(String userId, String content, LocalDate date) {
        this.userId = userId;
        this.content = content;
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getLocalDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
