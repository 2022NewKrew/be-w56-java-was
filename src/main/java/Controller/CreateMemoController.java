package Controller;

import static webserver.http.HttpMeta.NO_SESSION;

import db.SessionStorage;
import db.UserStorage;
import java.sql.SQLException;
import java.util.Map;
import model.CreateMemoRequest;
import model.Session;
import model.User;
import service.CreateMemoService;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

public class CreateMemoController implements Controller {

    @Override
    public void process(HttpRequest request, HttpResponse response) throws SQLException, ClassNotFoundException {
        String writer = getWriter(request);
        if (writer == null) {
            response.redirectLoginPage();
            return;
        }

        Map<String, String> queryData = request.getQueryData();

        CreateMemoRequest createMemoRequest = CreateMemoRequest.from(writer, queryData);
        CreateMemoService.createMemo(createMemoRequest);
        response.redirectBasicPage();
    }

    private String getWriter(HttpRequest request) throws SQLException, ClassNotFoundException {
        String userId = getUserId(request);
        if (userId == null) {
            return null;
        }

        User user = UserStorage.findUserById(userId);
        if (user == null) {
            return null;
        }
        return user.getName();
    }

    private String getUserId(HttpRequest request) throws SQLException, ClassNotFoundException {
        int sessionId = request.getSessionId();
        if (sessionId == NO_SESSION) {
            return null;
        }

        Session session = SessionStorage.findSessionById(sessionId);
        if (session == null || session.isExpired()) {
            return null;
        }

        return session.getUserId();
    }
}
