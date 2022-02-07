package view;

import dto.ResponseBodyDto;
import model.Memo;
import model.User;
import org.apache.tika.Tika;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

public class HtmlView {

    private static final Tika tika = new Tika();

    public ResponseBodyDto staticResourceView(String path) throws IOException {
        File file = new File("./webapp" + path);
        Path filePath = file.toPath();

        byte[] body = Files.readAllBytes(filePath);

        String contentType = Files.probeContentType(filePath);
        if (contentType == null || contentType.equals("")) {
            contentType = tika.detect(file);
        }

        return new ResponseBodyDto(body, contentType);
    }

    public byte[] userListView(Collection<User> users) throws IOException {
        File file = new File("./webapp" + "/user/list.html");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();
        StringBuilder sb = new StringBuilder();

        makeListView(users, br, line, sb);

        return sb.toString().getBytes();
    }

    public byte[] indexView(Collection<Memo> memos) throws IOException {
        File file = new File("./webapp" + "/index.html");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();
        StringBuilder sb = new StringBuilder();

        makeIndexView(memos, br, line, sb);

        return sb.toString().getBytes();
    }

    private void makeListView(Collection<User> users, BufferedReader br, String line, StringBuilder sb) throws IOException {
        while (line != null) {
            sb.append(line).append("\r\n");
            if (line.contains("<tbody>")) {
                int cnt = 1;
                for (User user : users) {
                    sb.append("<tr>\r\n");
                    sb.append("<th scope=\"row\">").append(cnt).append("</th>\r\n");
                    sb.append("<td>").append(user.getUserId()).append("</td>\r\n");
                    sb.append("<td>").append(user.getName()).append("</td>\r\n");
                    sb.append("<td>").append(user.getEmail()).append("</td>\r\n");
                    cnt++;
                }
            }
            line = br.readLine();
        }
    }

    private void makeIndexView(Collection<Memo> memos, BufferedReader br, String line, StringBuilder sb) throws IOException {
        while (line != null) {
            sb.append(line).append("\r\n");
            if (line.contains("<tbody>")) {
                int cnt = 1;
                for (Memo memo : memos) {
                    sb.append("<tr>\r\n");
                    sb.append("<th scope=\"row\">").append(cnt).append("</th>\r\n");
                    sb.append("<td>").append(memo.getCreatedAt()).append("</td>\r\n");
                    sb.append("<td>").append(memo.getName()).append("</td>\r\n");
                    sb.append("<td>").append(memo.getContent()).append("</td>\r\n");
                    cnt++;
                }
            }
            line = br.readLine();
        }
    }

}
