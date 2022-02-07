package template;

import java.lang.reflect.Field;

public class ParseString {
    private final MarkType type;
    private final String string;

    private ParseString(MarkType type, String string) {
        this.type = type;
        this.string = string;
    }

    public static ParseString parse(int index, String string) {
        boolean isOddIndex = index % 2 == 1;
        return new ParseString(MarkType.match(isOddIndex, string), string);
    }

    public String parseName() {
        return type.nameOf(string);
    }

    @Override
    public String toString() {
        return string;
    }

    public String replaceObject(Object object, int index) {
        if (type == MarkType.NONE) {
            return string;
        }
        if (type == MarkType.VALUE && parseName().equals("@index")) {
            return Integer.toString(index);
        }
        return findValue(object);
    }

    private String findValue(Object value) {
        try {
            Class<?> valueClass = value.getClass();
            Field field = valueClass.getDeclaredField(parseName());
            field.setAccessible(true);
            return field.get(value).toString();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return "";
        }
    }

    public boolean typeOf(MarkType type) {
        return this.type == type;
    }
}
