package util.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class ModelAndView {
    private final Map<String, String> model = new HashMap<>();
    private final String viewName;

    public void addAttribute(String key, String value){
        model.put(key, value);
    }
}
