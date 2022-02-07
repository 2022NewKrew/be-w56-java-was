package util.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class ModelAndView {
    private final Map<String, Object> model = new HashMap<>();
    private final String viewName;

    public void addAttribute(String key, Object value){
        model.put(key, value);
    }
}
