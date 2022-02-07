package template;

public enum MarkType {
    HTML("> ", true),
    LIST_START("#", true),
    LIST_END("/#", true),
    VALUE(null, true),
    NONE(null, false);

    private final String mark;
    private final boolean isOddIndex;

    MarkType(String mark, Boolean isOddIndex) {
        this.mark = mark;
        this.isOddIndex = isOddIndex;
    }

    public static MarkType match(boolean isOddIndex, String string) {
        if (!isOddIndex) {
            return NONE;
        }

        MarkType match = VALUE;
        for (MarkType type : values()) {
            match = matchType(match, type, string);
        }
        return match;
    }

    public static MarkType matchType(MarkType match, MarkType compare, String string) {
        if (compare.mark != null && string.contains(compare.mark)) {
            return compare;
        }
        return match;
    }

    public String nameOf(String line) {
        if (mark != null) {
            return line.substring(mark.length());
        }
        return line;
    }
}
