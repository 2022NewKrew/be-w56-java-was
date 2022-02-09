package webserver.http.response.templateEngine;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TemplateParserTest {

    @Test
    void parse() {
        String body = "hello{{I'm Data}}and{{>this is import}}and then last~{{#this is loop}}";
//        String body = "{{/sadf}}hihihi{{"
        List<Node> nodes = TemplateParser.parse(body);
        System.out.println(body);
        nodes.stream().forEach(System.out::println);
    }
}
