package template;

import app.user.domain.User;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TemplateUtilsTest {

    private static final List<User> users = new ArrayList<>();
    private static final Model model = new Model();

    @BeforeAll
    static void init() {
        users.add(new User("hello1", "secret1", "hoodie", "hoodie@example.com"));
        users.add(new User("hello2", "secret2", "hoodie1", "hoodie1@example.com"));
        model.addAttributes("user", users);
    }

    @Test
    void matching() {
        String sampleTemplate = "{{#user}}"
            + "<div>{id}</div>"
            + "<div>{name}</div>"
            + "<div>{email}</div>"
            + "{{/user}}";
        Assertions.assertNotEquals(sampleTemplate,
            TemplateUtils.mappingModel(sampleTemplate, model));
    }
}