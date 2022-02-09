package view;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.List;
import model.User;

public class View {

    public static byte[] staticFile(String filePath) throws IOException {
        return Files.readAllBytes(new File("./webapp" + filePath).toPath());
    }

    private static String userTable(List<User> userList) {
        StringBuilder userTable = new StringBuilder();
        for (User user : userList) {
            userTable.append("<tr>");
            userTable.append("<td> " + user.getUserId() + " </td>");
            userTable.append("<td> " + user.getName() + " </td>");
            userTable.append("<td> " + user.getEmail() + " </td>");
            userTable.append("</tr>");
        }
        return userTable.toString();
    }

    private static String convertFileToString(String filePath) throws IOException {
        byte[] htmlBytes = Files.readAllBytes(new File("./webapp" + filePath).toPath());
        return new String(htmlBytes);
    }

    public static byte[] userList(List<User> userList) throws IOException {
        System.out.println(convertFileToString("/user/list.html")
                .replace("{{userList}}", userTable(userList)));

        return convertFileToString("/user/list.html")
                .replace("{{userList}}", userTable(userList)).getBytes();
    }

    public static void sendResponse(OutputStream out, byte[] message) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        dos.write(message);
        dos.flush();
    }
}
