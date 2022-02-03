package template;

import db.DataBase;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TemplateEngine {

    public String render(String template) {
        String regex = "\\{\\{#users}}(.+)\\{\\{/users}}";
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(template);
        if (!matcher.find()) {
            return template;
        }
        String found = matcher.group(1);
        AtomicInteger i = new AtomicInteger(0);
        String section = DataBase.findAll()
                .stream()
                .map(user -> found.replaceAll("\\{\\{id}}", String.valueOf(i.incrementAndGet()))
                        .replaceAll("\\{\\{name}}", user.getName())
                        .replaceAll("\\{\\{userId}}", user.getUserId())
                        .replaceAll("\\{\\{email}}", user.getEmail()))
                .collect(Collectors.joining("\n"));
        return matcher.replaceAll(section);
    }
}
