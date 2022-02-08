package controller;

import httpmodel.HttpRequest;
import httpmodel.HttpResponse;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import model.Memo;
import service.MemoService;
import util.FileConverter;

public class FrontController extends AbstractController {

    private static final List<String> INDEX = List.of("/", "/index.html");

    private final MemoService memoService;

    public FrontController(MemoService memoService) {
        this.memoService = memoService;
    }

    @Override
    protected void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        try {
            byte[] responseBody = getResponseBody(httpRequest);
            httpResponse.set200OK(httpRequest, responseBody);
        } catch (NullPointerException exception) {
            throw new IllegalArgumentException("[ERROR] 해당파일은 존재하지 않습니다.");
        }
    }

    private byte[] getResponseBody(HttpRequest request) throws IOException {
        String uri = request.getUri();
        if (INDEX.contains(uri)) {
            BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream("webapp/index.html")));
            String file = createHtml(bufferedReader);
            bufferedReader.close();
            return file.getBytes(StandardCharsets.UTF_8);
        }
        return FileConverter.fileToString(uri);
    }

    private String createHtml(BufferedReader bufferedReader) throws IOException {
        StringJoiner stringJoiner = new StringJoiner("\r\n");
        String line = bufferedReader.readLine();
        while (Objects.nonNull(line)) {
            stringJoiner.add(line);
            putInfoIfLineEqualsTbody(line, stringJoiner);
            line = bufferedReader.readLine();
        }
        return stringJoiner.toString();
    }

    private void putInfoIfLineEqualsTbody(String line, StringJoiner stringJoiner) {
        if (!line.contains("<tbody>")) {
            return;
        }
        List<Memo> memos = memoService.findAll();
        for (Memo memo : memos) {
            stringJoiner.add("<tr>")
                .add("<td>" + memo.getCreatedAt() +
                    "</td> <td>" + memo.getUserId() +
                    "</td> <td>" + memo.getMemo() +
                    "</td>")
                .add("<tr/>");
        }
    }

    @Override
    protected void doPost(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        throw new IllegalArgumentException("[ERROR] 파일은 post 요청할 수 없습니다.");
    }
}
