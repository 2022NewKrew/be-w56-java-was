package was.templateEngine;

import application.domain.User;
import org.junit.jupiter.api.Test;
import templateEngine.CyEngineCompiler;
import templateEngine.CyEngineNode;
import templateEngine.CyEngineParser;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class CyEngineParserTest {

    @Test
    void parse() {
        final CyEngineParser cyEngineParser = new CyEngineParser();
        final String url = "/Users/kakao/Desktop/기술온보딩/be-w56-java-was/src/main/resources/login.html";

        final CyEngineNode cyEngineNode = cyEngineParser.parse(Path.of(url));

        final CyEngineCompiler compiler = new CyEngineCompiler();

        final Map<String, Object> model = new HashMap<>();
        model.put("hi", "<div> 안녕 안녕 </div>");

        final List<User> users = new ArrayList<>() {
            {
                add(new User("test1", "test1"));
                add(new User("test2", "test3"));
                add(new User("test3", "test4"));
            }
        };

        model.put("users", users);
        model.put("user0", users.get(0));
        model.put("node", cyEngineNode);

        final byte[] result = compiler.compile(cyEngineNode, model);

        System.out.println(new String(result, StandardCharsets.UTF_8));
    }
}
