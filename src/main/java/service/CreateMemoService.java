package service;

import db.MemoStorage;
import java.sql.SQLException;
import java.time.LocalDateTime;
import model.CreateMemoRequest;
import model.Memo;

public class CreateMemoService {

    public static void createMemo(CreateMemoRequest createMemoRequest) throws SQLException, ClassNotFoundException {
        String writer = createMemoRequest.getWriter();
        String content = createMemoRequest.getContent();

        if (content.isEmpty()) {
            return;
        }

        Memo memo = new Memo(writer, content, LocalDateTime.now());
        MemoStorage.addMemo(memo);
    }
}
