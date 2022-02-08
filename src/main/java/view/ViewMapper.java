package view;

import java.util.HashMap;
import java.util.Map;

public final class ViewMapper {

    private static final Map<String, String> viewMap = new HashMap<>();

    private ViewMapper() {

    }

    public static void addView(String path, String value) {
        viewMap.put(path, value);
    }

    public static String getView(String path) {
        return viewMap.get(path);
    }
}
