package adaptor.in.web;

import adaptor.in.web.exception.FileNotFoundException;
import adaptor.in.web.exception.UriNotFoundException;
import application.in.memo.ReadMemoUseCase;
import domain.memo.Memo;
import infrastructure.model.*;
import infrastructure.util.HtmlTemplateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);
    private final ReadMemoUseCase readMemoUseCase;

    public HomeController(ReadMemoUseCase readMemoUseCase) {
        this.readMemoUseCase = readMemoUseCase;
    }

    public HttpResponse handle(HttpRequest httpRequest) throws FileNotFoundException, UriNotFoundException {
        Path path = httpRequest.getRequestPath();

        try {
            if (path.matchHandler("")) {
                return home();
            }
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
        throw new UriNotFoundException();
    }

    public HttpResponse home() throws IOException {
        List<Memo> memos = readMemoUseCase.readAllMemo();
        List<Model> models = memos.stream().map(e -> Model.builder()
                .addAttribute("writer", e.getWriter())
                .addAttribute("createdAt", e.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .addAttribute("content", e.getContent())
                .build()
        ).collect(Collectors.toList());
        byte[] bytes = HtmlTemplateUtils.getView("/index.html", "memos", models);

        return HttpResponse.builder()
                .status(HttpStatus.OK)
                .body(new HttpByteArrayBody(bytes))
                .build();
//        );
    }
}
