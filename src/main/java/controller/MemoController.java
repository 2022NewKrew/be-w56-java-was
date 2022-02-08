package controller;

import annotation.Auth;
import annotation.RequestMapping;
import domain.UserId;
import dto.MemoDto;
import dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.MemoService;
import service.UserService;
import webserver.RequestHandler;
import webserver.http.HttpMethod;
import webserver.http.HttpStatus;
import webserver.request.Request;
import webserver.response.Response;
import webserver.response.ResponseBuilder;

import java.io.IOException;
import java.util.Map;

public class MemoController extends Controller {

    private static final MemoController memoController = new MemoController();
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private final MemoService memoService = MemoService.getInstance();
    private final UserService userService = UserService.getInstance();

    private MemoController() {
    }

    public static MemoController getInstance() {
        return memoController;
    }

    @Override
    public Response view(Request request, String url) throws IOException {
        log.debug("memo url : {}", url);
        return super.view(request, url);
    }

    @Auth
    @RequestMapping(url = "/create", method = HttpMethod.POST)
    public Response createMemo(Map<String, String> parameters) {
        String contents = parameters.get("contents");
        UserId userId = new UserId(parameters.get("cookie"));
        UserDto userDto = userService.findById(userId);
        MemoDto memoDto = new MemoDto(userDto.getName().toString(), contents, userDto);
        memoService.save(memoDto);
        return new ResponseBuilder().setHttpStatus(HttpStatus.FOUND)
                .addHeader("location", "/")
                .build();
    }
}
