package template;

public class ValueTemplate implements Template {
    private final ParseString parseString;
    private final Object value;

    public ValueTemplate(ParseString parseString, Object value) {
        this.parseString = parseString;
        this.value = value;
    }

    @Override
    public String load() {
        return null;
    }
}
