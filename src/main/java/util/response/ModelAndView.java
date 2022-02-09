package util.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class ModelAndView {
    private static ModelAndView modelAndView = null;

    private final Map<String, Object> model = new HashMap<>();
    private final String fileName;

    public void addAttribute(String key, Object value){
        model.put(key, value);
    }

    public static ModelAndView emptyModelAndView(){
        if(modelAndView == null){
            modelAndView = new ModelAndView("");
        }

        return modelAndView;
    }
}
