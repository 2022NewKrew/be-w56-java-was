package model;

import java.time.LocalDateTime;

public class Memo {

    private String name;
    private String content;
    private LocalDateTime date;

    public Memo(String name, String content, LocalDateTime date) {
        this.name = name;
        this.content = content;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
