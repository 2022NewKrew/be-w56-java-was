package service;

import model.Memo;
import model.User;
import repository.InMemoryMemoRepository;
import repository.InMemoryUserRepository;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class ViewService {

    private static final String USER_LIST_VIEW_PATH = "/user/list.html";
    private static final String USER_LIST_TARGET = "{{userList}}";
    private static final String MEMO_LIST_TARGET = "{{memoList}}";

    private static final InMemoryUserRepository userRepository = InMemoryUserRepository.getInstance();
    private static final InMemoryMemoRepository memoRepository = InMemoryMemoRepository.getInstance();

    public static byte[] getView(String url) throws IOException {
        String baseHtml = new String(Files.readAllBytes(new File("./webapp" + url).toPath()));
        if (Objects.equals(url, USER_LIST_VIEW_PATH)) {
            String userListview = baseHtml.replace(USER_LIST_TARGET, getUserListHtml());
            return userListview.getBytes(StandardCharsets.UTF_8);
        }
        String memoListView = baseHtml.replace(MEMO_LIST_TARGET, getMemoListHtml());
        return memoListView.getBytes(StandardCharsets.UTF_8);
    }

    private static String getUserListHtml() {
        List<User> users = new ArrayList<>(userRepository.findAll());

        AtomicInteger atomicInteger = new AtomicInteger();
        StringBuilder sb = new StringBuilder();
        users.forEach(user -> {
            sb.append("<tr>\n").append("<th scope=\"row\">").append(atomicInteger.incrementAndGet()).append("</th>\n");
            sb.append("<td>").append(user.getUserId()).append("</td>\n");
            sb.append("<td>").append(user.getName()).append("</td>\n");
            sb.append("<td>").append(user.getEmail()).append("</td>\n");
            sb.append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n").append("</tr>\n");
        });

        return sb.toString();
    }

    private static String getMemoListHtml() {
        List<Memo> memos = new ArrayList<>(memoRepository.findAll());

        StringBuilder sb = new StringBuilder();
        memos.forEach(memo -> {
            sb.append("<tr>\n");
            sb.append("<td>").append(memo.getCreatedAt()).append("</td>\n");
            sb.append("<td>").append(memo.getWriter()).append("</td>\n");
            sb.append("<td>").append(memo.getTitle()).append("</td>\n");
            sb.append("<td>").append(memo.getContent()).append("</td>\n");
            sb.append("</tr>\n");
        });

        return sb.toString();
    }


}
