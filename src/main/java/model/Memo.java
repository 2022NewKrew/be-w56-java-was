package model;

import java.net.URLDecoder;

public class Memo {
    private final String REGEX_WRITER = "^\\w+$";
    private final String REGEX_MEMO = "^[\\s\\S]+$";

    private int memoId;
    private String date;
    private String writer;
    private String memo;

    public Memo(int memoId, String date, String writer, String memo) throws Exception {
        this.memoId = memoId;
        this.date = date;
        this.writer = writer;
        this.memo = URLDecoder.decode(memo, "UTF-8");
    }

    public Memo(String writer, String memo) throws Exception {
//        if(checkRegexOfMemo(writer,memo)) {
//            throw new Exception();
//        }
        this.writer = writer;
        this.memo = URLDecoder.decode(memo, "UTF-8");
    }

    private boolean checkRegexOfMemo (String writer, String memo) {
        return checkRegexOfString(writer, REGEX_WRITER) && checkRegexOfString(memo, REGEX_MEMO);
    }

    private boolean checkRegexOfString(String str, String regex) {
        return str != null && str.matches(regex);
    }

    public int getMemoId() {
        return memoId;
    }

    public String getDate() {
        return date;
    }

    public String getWriter() {
        return writer;
    }

    public String getMemo() {
        return memo;
    }

    @Override
    public String toString() {
        return "Memo{" +
                "memoId=" + memoId +
                ", date='" + date + '\'' +
                ", writer='" + writer + '\'' +
                ", memo='" + memo + '\'' +
                '}';
    }
}
