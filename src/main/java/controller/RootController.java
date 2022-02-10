package controller;

import mapper.HtmlUseConst;
import mapper.ResponseSendDataModel;
import model.memo.Memo;
import model.memo.MemoDTO;
import model.user_account.UserAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.MemoService;
import service.SessionService;
import service.UserService;
import webserver.request.HttpRequest;
import webserver.session.Session;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class RootController implements Controller{
    private final Map<String, Function<HttpRequest, ResponseSendDataModel>> methodMapper;
    private final MemoService memoService;
    private final SessionService sessionService;
    private final Logger log = LoggerFactory.getLogger(RootController.class);

    public RootController(){
        methodMapper = new HashMap<>();
        memoService = new MemoService();
        sessionService = SessionService.getInstance();

        methodMapper.put("GET /", this::index);
        methodMapper.put("GET /index.html", this::index);
        methodMapper.put("POST /", this::postIndex);
    }

    private ResponseSendDataModel index(HttpRequest httpRequest){
        ResponseSendDataModel result = new ResponseSendDataModel("/index.html", httpRequest);

        List<Memo> memoList = memoService.findAll();
        Collections.reverse(memoList);

        result.add(HtmlUseConst.MEMO_LIST, memoList);

        return result;
    }

    private ResponseSendDataModel postIndex(HttpRequest httpRequest){
        ResponseSendDataModel result = new ResponseSendDataModel("redirect:/", httpRequest);

        Map<String, String> model = httpRequest.getBody();

        String contents = model.get("contents");

        Optional<Session> sessionedId = (Optional<Session>) result.get("sessionedId");
        if(sessionedId.isEmpty()){
            log.info("로그인하지 않은 사용자가 유저 목록에 접근하였습니다");

            return result;
        }

        Session session = sessionedId.get();

        MemoDTO memoDTO = new MemoDTO.Builder()
                .setWriter(session.getUserAccount().getUserId())
                .setContents(contents).build();

        memoService.join(memoDTO);

        return result;
    }

    @Override
    public Function<HttpRequest, ResponseSendDataModel> decideMethod(String method, String url) {
        url = method + " " + url;

        return methodMapper.get(url);
    }
}
