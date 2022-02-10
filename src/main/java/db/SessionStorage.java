package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import model.Session;

public class SessionStorage {

    private static final DBConnection dbConn = new DBConnection();

    public static void addSession(Session session) throws SQLException, ClassNotFoundException {
        dbConn.connect();
        String sql = "INSERT INTO session VALUES(" +
                     "'" + session.getSessionId() + "'" + "," +
                     "'" + session.getUserId() + "'" + "," +
                     "'" + Timestamp.valueOf(session.getExpire()) + "'" + ")";
        Statement statement = dbConn.getConnection().createStatement();
        statement.executeUpdate(sql, Statement.NO_GENERATED_KEYS);
        statement.close();
        dbConn.close();
    }

    public static void updateSession(Session session) throws SQLException, ClassNotFoundException {
        dbConn.connect();
        String sql = "UPDATE session SET expire=" +
                     "'" + Timestamp.valueOf(session.getExpire()) + "'" +
                     " WHERE sessionId=" + "'" + session.getSessionId() + "'";
        Statement statement = dbConn.getConnection().createStatement();
        statement.executeUpdate(sql, Statement.NO_GENERATED_KEYS);
        statement.close();
        dbConn.close();
    }

    public static Session findSessionById(int sessionId) throws SQLException, ClassNotFoundException {
        dbConn.connect();
        String sql = "SELECT * FROM session WHERE sessionId='" + sessionId + "'";
        Statement statement = dbConn.getConnection().createStatement();
        ResultSet rs = statement.executeQuery(sql);

        if (!rs.next()) {
            return null;
        }

        String userId = rs.getString("userId");
        LocalDateTime expire = rs.getTimestamp("expire").toLocalDateTime();

        statement.close();
        dbConn.close();
        return new Session(sessionId, userId, expire);
    }
}
