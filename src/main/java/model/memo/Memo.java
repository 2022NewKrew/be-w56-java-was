package model.memo;

import java.time.LocalDateTime;

public class Memo {
    private final int id;
    private final String writer;
    private final LocalDateTime date;
    private final String contents;

    public Memo(MemoDTO memoDTO, int id) {
        this.id = id;
        this.writer = memoDTO.getWriter();
        this.date = memoDTO.getDate();
        this.contents = memoDTO.getContents();
    }

    public String getId() {
        return "" + id;
    }

    public String getWriter() {
        return writer;
    }

    public String getDate() {
        return date.toString();
    }

    public String getContents() {
        return contents;
    }
}
