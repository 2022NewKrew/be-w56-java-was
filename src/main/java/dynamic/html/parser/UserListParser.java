package dynamic.html.parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.IntStream;

import http.Response;
import model.User;

public class UserListParser extends DefaultParser {
    public byte[] getPage(Response response, String url) {
        StringBuilder stringBuilder = new StringBuilder();

        Object[] userList = response.getAttributes("Users").toArray();

        try {
            Files.lines(new File(SOURCE_ROOT + url).toPath()).forEach(line->{
                stringBuilder.append(line);

                if (line.contains("<tbody>")) {
                    IntStream.range(0, userList.length).forEach(index-> {
                        User user = (User) userList[index];
                        stringBuilder.append("<tr >")
                                     .append("<th scope=\"row\">").append(index).append("</th>")
                                     .append("<td > ").append(user.getUserId()).append(" </td >")
                                     .append("<td > ").append(user.getName()).append(" </td >")
                                     .append("<td > ").append(user.getEmail()).append(" </td >")
                                     .append("<td ><a href = \"#\" class=\"btn btn-success\" role = \"button\" > 수정 </a ></td >")
                                     .append("</tr >");
                    });
                }
            });

            return stringBuilder.toString().getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.getPage(response, url);
    }
}
