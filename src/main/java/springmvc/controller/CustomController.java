package springmvc.controller;

import java.util.Map;

public interface CustomController {
    String process(Map<String, String> paramMap, Map<String, Object> model);
}
