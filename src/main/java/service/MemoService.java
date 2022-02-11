package service;

import model.Memo;
import model.Request;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.MemoRepository;
import repository.UserRepository;
import util.HttpRequestUtils;

import java.util.*;

public class MemoService {
    private static final Logger logger = LoggerFactory.getLogger(MemoService.class);
    private static final UserRepository userRepository;
    private static final MemoRepository memoRepository;

    static {
        userRepository = new UserRepository();
        memoRepository = new MemoRepository();
    }

    public static void createMemo(Request request) {
        Map<String, String> reqParam = HttpRequestUtils.parseQueryString(request.getBody());

        String content = reqParam.get("content");
        String userName = reqParam.get("writer");

        User user = userRepository.findByName(userName).orElseThrow(() -> new NullPointerException("NoSuchUser"));
        Memo memo = new Memo(content, user);

        memoRepository.create(memo);
    }

    public static Collection<Memo> showMemos(Request request) {
        return memoRepository.findAll();
    }
}
