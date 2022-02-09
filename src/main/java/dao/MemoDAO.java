package dao;

import model.Memo;
import util.DBUtils;
import util.TimeStringParser;
import webserver.http.HttpRequest;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MemoDAO {
    private static final String STORE_SQL =
            "INSERT INTO MEMOS(WRITER, CONTENT, CREATED_DATE) VALUES(?, ?, ?)";
    private static final String TO_LIST_SQL =
            "SELECT * FROM MEMOS WHERE DELETED=FALSE";

    private final Connection connection;

    public MemoDAO() {
        try {
            connection = DBUtils.getConnection();
        } catch (SQLException e) {
            throw new IllegalStateException("DB connection failed");
        }
    }

    public void storeMemo(HttpRequest httpRequest) throws SQLException {
        Map<String, String> params = httpRequest.getParameters();

        PreparedStatement statement = connection.prepareStatement(STORE_SQL);
        statement.setString(1, URLDecoder.decode(params.get("writer"), StandardCharsets.UTF_8));
        statement.setString(2, URLDecoder.decode(params.get("contents"), StandardCharsets.UTF_8));
        statement.setString(3, TimeStringParser.parseTimeToString(LocalDateTime.now()));
        statement.execute();

        statement.close();
    }

    public List<Memo> findAllMemos() throws SQLException {
        List<Memo> memoList = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(TO_LIST_SQL);

        while (resultSet.next() && memoList.size() < 20) {
            Memo memo = Memo.builder()
                    .memoId(resultSet.getLong(1))
                    .writer(resultSet.getString(2))
                    .contents(resultSet.getString(3))
                    .time(resultSet.getString(4))
                    .build();
            memoList.add(memo);
        }

        resultSet.close();
        statement.close();
        return memoList;
    }
}
