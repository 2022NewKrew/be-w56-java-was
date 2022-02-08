package servlet;

import servlet.view.RedirectView;
import servlet.view.StaticView;
import servlet.view.TemplateView;
import servlet.view.View;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ResponseType {
    REDIRECT(String.class, "redirect:", true, RedirectView::new),
    STATIC_FILE(String.class, ".html", true, StaticView::new),
    TEMPLATE_FILE(String.class, ".html", false, TemplateView::new),
    ERROR(null, null, true, path -> null);

    private final Class<?> classType;
    private final String containWord;
    private final boolean isEmptyModel;
    private final Function<String, View> createView;

    ResponseType(Class<?> classType, String path, boolean isEmptyModel, Function<String, View> createView) {
        this.classType = classType;
        this.containWord = path;
        this.isEmptyModel = isEmptyModel;
        this.createView = createView;
    }

    private static ResponseType matchOf(Object response, boolean isExistModel) {
        List<ResponseType> match = Arrays.stream(values())
                .filter(type -> isMatch(type, response, isExistModel))
                .collect(Collectors.toList());

        if (match.isEmpty()) {
            return ERROR;
        }
        return match.get(0);
    }

    private static boolean isMatch(ResponseType type, Object response, boolean isExistModel) {
        return response.getClass() == type.classType
                && ((String) response).contains(type.containWord)
                && isExistModel == type.isEmptyModel;
    }

    public static View createView(Object response, boolean isExistModel) {
        ResponseType type = matchOf(response, isExistModel);
        String path = makePath(type, response);
        return type.createView.apply(path);
    }

    private static String makePath(ResponseType type, Object object) {
        String path = (String) object;
        if (type == REDIRECT) {
            return path.substring(type.containWord.length());
        }
        return path;
    }
}
