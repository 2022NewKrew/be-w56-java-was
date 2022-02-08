package dao;

public enum ArticleAttribute {
    ID("_id"), TITLE("title"), AUTHOR("author"), CONTENT("content"),
    CREATE_TIME("createTime"), MODIFIED_TIME("modifiedTime");

    private final String value;

    ArticleAttribute(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
