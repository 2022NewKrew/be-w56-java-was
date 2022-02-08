package template;

import servlet.view.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParseStrings {
    private final static String REGULAR_EXPRESSION = "\\{\\{|\\}\\}";
    private final List<ParseString> parseStrings;

    public ParseStrings(List<ParseString> parseStrings) {
        this.parseStrings = parseStrings;
    }

    public static ParseStrings parse(String string) {
        String[] splits = string.split(REGULAR_EXPRESSION);
        return new ParseStrings(IntStream.range(0, splits.length - 1)
                .mapToObj(index -> ParseString.parse(index, splits[index]))
                .collect(Collectors.toList()));
    }

    public Templates createTemplates(Model model) {
        List<Template> templates = new ArrayList<>();
        boolean isInList = false;
        List<ParseString> listTemplate = new ArrayList<>();
        for (ParseString parseString : parseStrings) {
            if (!isInList) {
                if (parseString.typeOf(MarkType.LIST_START)) {
                    isInList = true;
                }
                if (parseString.typeOf(MarkType.NONE)) {
                    templates.add(new StaticTemplate(parseString));
                }
                if (parseString.typeOf(MarkType.HTML)) {
                    templates.add(new HtmlTemplate(parseString));
                }
                if (parseString.typeOf(MarkType.VALUE)) {
                    Object value = model.getAttribute(parseString.parseName());
                    templates.add(new ValueTemplate(parseString, value));
                }
            } else {
                if (parseString.typeOf(MarkType.LIST_END)) {
                    isInList = false;
                    Object value = model.getAttribute(parseString.parseName());
                    templates.add(new ListTemplate(listTemplate, value));
                    listTemplate = new ArrayList<>();
                } else {
                    listTemplate.add(parseString);
                }
            }
        }
        return new Templates(templates);
    }

    public String concat(Object object) {
        return IntStream.range(0, parseStrings.size() - 1)
                .mapToObj(index -> parseStrings.get(index).replaceObject(object, index))
                .collect(Collectors.joining());
    }
}
