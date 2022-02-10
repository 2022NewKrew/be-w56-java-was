package adaptor.in.web.memo;

import adaptor.in.web.exception.FileNotFoundException;
import adaptor.in.web.exception.UriNotFoundException;
import adaptor.in.web.model.RequestPath;
import application.exception.user.NonExistsUserIdException;
import application.in.memo.ReadMemoUseCase;
import application.in.memo.WriteMemoUseCase;
import application.in.session.GetSessionUseCase;
import domain.memo.Memo;
import domain.session.Session;
import infrastructure.model.*;
import infrastructure.util.HttpRequestUtils;
import infrastructure.util.HttpResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

public class MemoController {

    private final Logger log = LoggerFactory.getLogger(MemoController.class);
    private final GetSessionUseCase getSessionUseCase;
    private final WriteMemoUseCase writeMemoUseCase;
    private final ReadMemoUseCase readMemoUseCase;

    public MemoController(GetSessionUseCase getSessionUseCase, WriteMemoUseCase writeMemoUseCase, ReadMemoUseCase readMemoUseCase) {
        this.getSessionUseCase = getSessionUseCase;
        this.writeMemoUseCase = writeMemoUseCase;
        this.readMemoUseCase = readMemoUseCase;
    }

    public HttpResponse handle(HttpRequest httpRequest) throws FileNotFoundException, UriNotFoundException {
        Path path = httpRequest.getRequestPath();
        RequestMethod method = httpRequest.getMethod();
        log.debug("User Controller: {}", path);

        try {
            if (RequestPath.WRITE_MEMO.equalsValue(path) && method.equals(RequestMethod.POST)) {
                return write(httpRequest);
            }
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
        throw new UriNotFoundException();
    }

    private HttpResponse write(HttpRequest request) throws IOException {
        String sessionId = HttpRequestUtils.parseCookies(request.getHeader("Cookie")).get("SESSION_ID");
        Session<String> session = getSessionUseCase.getSession(sessionId);
        String loginId = session.getAttributeValue();

        Map<String, String> body = HttpRequestUtils.parseBody(((HttpStringBody) request.getRequestBody()).getValue());

        Memo memo = Memo.builder()
                .writer(body.get("writer"))
                .content(body.get("contents"))
                .createdAt(LocalDateTime.now())
                .build();

        try {
            writeMemoUseCase.write(loginId, memo);

            return HttpResponseUtils.found("/index.html");
        } catch (NonExistsUserIdException e) {
            return HttpResponseUtils.badRequest();
        }
    }
}
