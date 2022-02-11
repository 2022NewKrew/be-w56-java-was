package cafe.view;

import cafe.dto.UserDto;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class UserView {
    private static final String WEBAPP_PATH = "/webapp";

    public String getUserListHtml(List<UserDto> userDtoList) throws IOException {
        InputStream resourceAsStream = this.getClass().getResourceAsStream(WEBAPP_PATH + "/user/list.html");
        byte[] bytes = resourceAsStream.readAllBytes();
        String htmlString = new String(bytes);

        StringBuilder usersHtml = new StringBuilder();

        AtomicInteger index = new AtomicInteger(1);
        userDtoList.stream()
                .forEach(userDto -> {
                    usersHtml.append("<tr>");
                    usersHtml.append("<th scope=\"row\">").append(index.get()).append("</th>");
                    usersHtml.append("<td>").append(userDto.getUserId()).append("</td>");
                    usersHtml.append("<td>").append(userDto.getName()).append("</td>");
                    usersHtml.append("<td>").append(userDto.getEmail()).append("</td>");
                    usersHtml.append("<td><a class=\"btn btn-success\" href=\"#\" role=\"button\">수정</a></td>");
                    usersHtml.append("</tr>");
                    index.getAndIncrement();
                });
        return htmlString.replace("{{userList}}", usersHtml.toString());
    }

}
