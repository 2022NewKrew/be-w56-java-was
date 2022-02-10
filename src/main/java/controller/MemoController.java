package controller;

import jdbc.JedisPools;
import model.Memo;
import model.Pair;
import model.request.Body;
import model.request.Headers;
import model.request.HttpLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repo.MemoJdbc;
import repo.UserJdbc;
import util.HttpRequestUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MemoController {
    private static final Logger log = LoggerFactory.getLogger(MemoController.class);

    private static final String LOCATION_MEMO_LIST = "/index";
    private static final String LOCATION_MEMO_CREATE = "/memo/create";

    private final UserJdbc userJdbc;
    private final MemoJdbc memoJdbc;

    public MemoController(final JedisPools jedisPools) {
        this.userJdbc = new UserJdbc(jedisPools);
        this.memoJdbc = new MemoJdbc(jedisPools);
    }

//    public Body processGet(final String location) {
//        if (LOCATION_MEMO_LIST.equals(location)) {
//            return getUserList();
//        }
//
//        return Body.EMPTY;
//    }
//
//    static TemplateEngine userListTpl;
//    private Body getUserList() {
//        if (userListTpl == null) {
//            userListTpl = new TemplateEngine(LOCATION_MEMO_LIST);
//        }
//        TemplateAttribute<User> tplAttr = new TemplateAttribute<>();
//        tplAttr.set("memolist", userJdbc.findAll());
//        return userListTpl.processTemplate(tplAttr);
//    }

    public Headers processPost(final String location, final String userId, final Body body) {
        final List<Pair> list = new ArrayList<>();
        if (LOCATION_MEMO_CREATE.equals(location)) {
            memoCreate(list, userId, body);
        }

        return new Headers(list);
    }

    private void memoCreate(final List<Pair> list, final String userId, final Body body) {
        if (userId == null) {
            list.add(new Pair(Headers.HEADER_LOCATION, new HttpLocation("/user/login.html").getLocation()));
            return;
        }

        final String decodedParameters = URLDecoder.decode(body.get(), StandardCharsets.UTF_8);
        final Map<String, String> map = HttpRequestUtils.parseQueryString(decodedParameters);

        try {
            add(userId, map);
        } catch (IllegalStateException e) {
            return;
        }

        list.add(new Pair(Headers.HEADER_LOCATION, new HttpLocation("/index.html").getLocation()));
    }

    private void add(final String userId, final Map<String, String> parameterMap) {
        if (Objects.isNull(userJdbc.findUserById(userId))) {
            throw new IllegalStateException("User with id(" + userId + ") is not exist!");
        }

        final Memo memo = new Memo(userId, parameterMap.get("memo"));
        memoJdbc.addMemo(memo);
        log.info("New memo added by " + userId + "!");
    }
}
