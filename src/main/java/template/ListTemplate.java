package template;

import java.util.Collection;
import java.util.List;

public class ListTemplate implements Template {
    private final ParseStrings parseStrings;
    private final Object list;

    private ListTemplate(ParseStrings parseStrings, Object list) {
        this.parseStrings = parseStrings;
        this.list = list;
    }

    public ListTemplate(List<ParseString> parseStrings, Object list) {
        this(new ParseStrings(parseStrings), list);
    }

    @Override
    public String load() {
        StringBuilder sb = new StringBuilder();
        for (Object value : (Collection<?>) list) {
            sb.append(parseStrings.concat(value));
        }
        return sb.toString();
    }
}
