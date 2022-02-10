package Controller;

import static webserver.http.HttpMeta.MIME_TYPE_OF_HTML;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import model.Memo;
import service.GetMemoListService;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

public class PrintBasicPageController implements Controller {

    private static final int NUMBER__OF_DISPLAY_MEMO = 10;
    private static final String TO_BE_REPLACE_STRING = "{{memoList}}";
    private static final File memoListFile = new File("./webapp/index.html");

    @Override
    public void process(HttpRequest request, HttpResponse response)
        throws IOException, SQLException, ClassNotFoundException {
        List<Memo> memoList = GetMemoListService.getMemoList();
        Collections.reverse(memoList);
        String body = getBody(memoList.subList(0, Math.min(memoList.size(), NUMBER__OF_DISPLAY_MEMO)));

        buildResponse(response, body);
    }

    private void buildResponse(HttpResponse response, String body) {
        response.setContentType(MIME_TYPE_OF_HTML);
        response.setContentLength(body.length());
        response.setMessage(body);
    }

    private String getBody(List<Memo> memoList) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(memoListFile.getPath()));
        String memoListStr = getMemoListString(memoList);
        return lines.stream()
                    .map(s -> s.trim().equals(TO_BE_REPLACE_STRING) ? memoListStr : s)
                    .collect(Collectors.joining("\n"));
    }

    private String getMemoListString(List<Memo> memoList) {
        StringBuilder sb = new StringBuilder();
        for (Memo memo : memoList) {
            sb.append("\n<tr>")
              .append("<td>")
              .append(memo.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
              .append("</td>")
              .append("<td>").append(memo.getWriter()).append("</td>")
              .append("<td>").append(memo.getContent()).append("</td>\n")
              .append("</tr>\n");
        }
        return sb.toString();
    }
}
