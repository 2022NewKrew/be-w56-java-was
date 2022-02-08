package webserver.framwork.core.templateengine;

public class MustacheBlock {
    public static final int BASKET_LENGTH = 2;
    public static final String START_BASKET = "{{";
    public static final String END_BASKET = "}}";
    public static final String SCOPE_START_TAG = "#";
    public static final String SCOPE_END_TAG = "/";

    private String line;
    private boolean hasMustache = false;
    private int startBasketIndex;
    private int endBasketIndex;

    public MustacheBlock(String line) {
        int startBasketIndex = line.indexOf(START_BASKET);
        int endBasketIndex = line.indexOf(END_BASKET);

        this.line = line;
        if (startBasketIndex < endBasketIndex && startBasketIndex != -1) {
            this.hasMustache = true;
            this.startBasketIndex = startBasketIndex;
            this.endBasketIndex = endBasketIndex;
        }
    }

    public String getIdentifier() {
        if (!hasMustache) {
            return "";
        }

        if (isScopeStart() || isScopeEnd()) {
            return line.substring(startBasketIndex + BASKET_LENGTH + 1, endBasketIndex);
        }
        return line.substring(startBasketIndex + BASKET_LENGTH, endBasketIndex);
    }

    public boolean isScopeStart() {
        return hasMustache &&
                line.substring(startBasketIndex + BASKET_LENGTH).startsWith(SCOPE_START_TAG);
    }

    public boolean isScopeEnd() {
        return hasMustache &&
                line.substring(startBasketIndex + BASKET_LENGTH).startsWith(SCOPE_END_TAG);
    }

    public int length() {
        return endBasketIndex - startBasketIndex + BASKET_LENGTH;
    }

    public boolean hasMustache() {
        return hasMustache;
    }

    public String toString() {
        return line;
    }

    public String replaceWith(String newString) {
        if (!hasMustache) {
            return line;
        }
        StringBuffer sb = new StringBuffer(line);
        sb.replace(startBasketIndex, endBasketIndex + BASKET_LENGTH, newString);
        return sb.toString();
    }
}
