package adaptor.in.web.memo;

import adaptor.in.web.exception.FileNotFoundException;
import adaptor.in.web.exception.UriNotFoundException;
import adaptor.in.web.model.RequestPath;
import application.exception.user.NonExistsUserIdException;
import application.in.memo.WriteMemoUseCase;
import application.in.session.GetSessionUseCase;
import domain.memo.Memo;
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

    public MemoController(GetSessionUseCase getSessionUseCase, WriteMemoUseCase writeMemoUseCase) {
        this.getSessionUseCase = getSessionUseCase;
        this.writeMemoUseCase = writeMemoUseCase;
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
        /*
        * 로그인 한 사용자만 글을 쓸 수 있음
        * 요청에서 쿠키 꺼내서 세션 아이디 유효한지 확인
        * 글 저장하기
        * */
        Map<String, String> body = HttpRequestUtils.parseBody(((HttpStringBody) request.getRequestBody()).getValue());

        Memo memo = Memo.builder()
                .writer(body.get("writer"))
                .content(body.get("contents"))
                .createdAt(LocalDateTime.now())
                .build();

        try {
            writeMemoUseCase.write("id", memo);

            return HttpResponseUtils.found("/index.html");
        } catch (NonExistsUserIdException e) {
            return HttpResponseUtils.badRequest();
        }
    }
}
