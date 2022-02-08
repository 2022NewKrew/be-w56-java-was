package webapplication.data;

public enum AttributeTypes {

    COOKIES("COOKIES");

    private String code;

    AttributeTypes(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
