package template;

public class StaticTemplate implements Template {
    private final ParseString parseString;

    public StaticTemplate(ParseString parseString) {
        this.parseString = parseString;
    }

    public static Template create(ParseString parseString) {
        return new StaticTemplate(parseString);
    }

    @Override
    public String load() {
        return parseString.toString();
    }
}
