package model;

public class Memo {
    private String date;
    private String writer;
    private String context;

    public Memo(String date, String writer, String context) {
        this.date = date;
        this.writer = writer;
        this.context = context;
    }

    public String getDate() {
        return date;
    }

    public String getWriter() {
        return writer;
    }

    public String getContext() {
        return context;
    }
}
