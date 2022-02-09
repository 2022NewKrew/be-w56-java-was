package view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;

public class TableComponentFrame {

    private static final Logger log = LoggerFactory.getLogger(TableComponentFrame.class);

    public static String thead(String ...headers) {
        StringBuilder sb = new StringBuilder();
        sb.append("<thead>\n");
        sb.append("<tr>\n");
        for (String header : headers) {
            sb.append("<th>");
            sb.append(header);
            sb.append("</th>\n");
        }
        sb.append("</tr>\n");
        sb.append("</thead>\n");
        return sb.toString();
    }

    public static String tbody(Object body, String ...columnName) {
        List<?> bodies;
        if (body instanceof List) {
            bodies = (List<?>) body;
        }
        else {
            throw new IllegalArgumentException("Object가 List 타입이 아닙니다");
        }

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("<tbody>\n");
            for (int i = 0; i < bodies.size(); i++) {
                sb.append("<tr>");
                sb.append("<th scope=\"row\">");
                sb.append(i + 1);
                sb.append("</th>");
                for (String name : columnName) {
                    sb.append("<td>");
                    Field field = bodies.get(i).getClass().getDeclaredField(name);
                    field.setAccessible(true);
                    sb.append(field.get(bodies.get(i)).toString());
                    sb.append("</td>");
                }
                sb.append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>");
                sb.append("</tr>\n");
            }
            sb.append("</tbody>\n");
            return sb.toString();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return "";
        }
    }
}
