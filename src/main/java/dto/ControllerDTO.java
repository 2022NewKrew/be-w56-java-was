package dto;

import java.util.Map;

public class ControllerDTO {
    String path;
    Map<String, String> element;

    public ControllerDTO(String path, Map<String, String> element) {
        this.path = path;
        this.element = element;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getElement() {
        return element;
    }
}
