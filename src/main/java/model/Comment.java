package model;

import lombok.Getter;

@Getter
public class Comment {
    private String registerDate;
    private String writer;
    private String comment;

    public Comment(String registerDate, String writer, String comment) {
        this.registerDate = registerDate;
        this.writer = writer;
        this.comment = comment;
    }
}
