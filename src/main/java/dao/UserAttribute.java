package dao;

public enum UserAttribute {
    USER_ID("userId"), PASSWORD("password"), NAME("name"), EMAIL("email"),
    CREATE_TIME("createTime"), MODIFIED_TIME("modifiedTime");

    private final String value;

    UserAttribute(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
