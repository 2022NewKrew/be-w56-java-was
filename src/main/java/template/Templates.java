package template;

import servlet.Model;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class Templates {
    private final List<Template> templates;

    public Templates(List<Template> templates) {
        this.templates = templates;
    }

    public byte[] concat(Model model) {
        StringBuilder sb = new StringBuilder();
        for (Template template : templates) {
            sb.append(template.load());
        }
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }
}
