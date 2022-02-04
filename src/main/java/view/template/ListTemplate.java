package view.template;

import java.util.List;
import java.util.Map;
import model.User;

public class ListTemplate implements ViewTemplate {

    private static ListTemplate instance;

    public static synchronized ListTemplate getInstance() {
        if (instance == null) {
            instance = new ListTemplate();
        }
        return instance;
    }

    private static final String block =
            "<div class=\"container\" id=\"main\">\n"
                    + "   <div class=\"col-md-10 col-md-offset-1\">\n"
                    + "      <div class=\"panel panel-default\">\n"
                    + "          <table class=\"table table-hover\">\n"
                    + "              <thead>\n"
                    + "                <tr>\n"
                    + "                    <th>#</th> <th>사용자 아이디</th> <th>이름</th> <th>이메일</th><th></th>\n"
                    + "                </tr>\n"
                    + "              </thead>\n"
                    + "              <tbody>\n"
                    + "%s" // userBlock
                    + "              </tbody>\n"
                    + "          </table>\n"
                    + "        </div>\n"
                    + "    </div>\n"
                    + "</div>\n"
                    + "\n";

    private static final String userBlock =
            "                <tr>\n"
                    + "                    <th scope=\"row\">%d</th> <td>%s</td> <td>%s</td> <td>%s</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n"
                    + "                </tr>\n";

    @Override
    public String getTemplate(Map<String, Object> model) {

        List<User> users = List.of();
        if (model.get("users") instanceof List && !((List<?>) model.get("users")).isEmpty()) {
            if (((List<?>) model.get("users")).get(0) instanceof User) {
                users = (List<User>) model.get("users");
            }
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < users.size(); i++) {
            result.append(
                    String.format(
                            userBlock, i + 1,
                            users.get(i).getUserId(),
                            users.get(i).getName(),
                            users.get(i).getEmail()));
        }
        return String.format(block, result);
    }
}
