package model.repository.memo;

import model.Memo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemoRepositoryJdbc implements MemoRepository {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://10.202.165.223:3306/kakao?serverTimezone=UTC";
    private static final String USERNAME = "kennypark";
    private static final String PASSWORD = "kennypark";

    @Override
    public Memo save(Memo memo) {
        if (memo.isNew()) {
            int id = insert(memo);
            return Memo.builder()
                    .id(id)
                    .userId(memo.getUserId())
                    .content(memo.getContent())
                    .build();
        }
        update(memo);
        return memo;
    }

    @Override
    public List<Memo> findAll() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sql = "SELECT M.ID, M.USER_ID, U.NAME as WRITER, M.CONTENT, M.CREATED_AT FROM `MEMO` M INNER JOIN `USER` U ON M.USER_ID = U.ID";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery(sql);
            List<Memo> memos = new ArrayList<>();
            while(rs.next()){
                memos.add(Memo.builder()
                        .id(rs.getInt(1))
                        .userId(rs.getInt(2))
                        .writer(rs.getString(3))
                        .content(rs.getString(4))
                        .createdAt(rs.getTimestamp(5).toLocalDateTime())
                        .build());
            }
            return memos;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        } finally {
            try {rs.close();} catch (Exception e) {}
            try {pstmt.close();} catch (Exception e) {}
            try {conn.close();} catch (Exception e) {}
        }
    }

    private int insert(Memo memo){
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sql = "INSERT INTO `MEMO`(USER_ID, CONTENT) VALUES (?, ?)";
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, memo.getUserId());
            pstmt.setString(2, memo.getContent());
            pstmt.executeUpdate();
            rs =  pstmt.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        } finally {
            try {rs.close();} catch (Exception e) {}
            try {pstmt.close();} catch (Exception e) {}
            try {conn.close();} catch (Exception e) {}
        }
    }

    private void update(Memo memo){
        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sql = "UPDATE `MEMO` SET USER_ID=?, CONTENT=? WHERE ID=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, memo.getUserId());
            pstmt.setString(2, memo.getContent());
            pstmt.setInt(3, memo.getId());
            pstmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        } finally {
            try {pstmt.close();} catch (Exception e) {}
            try {conn.close();} catch (Exception e) {}
        }
    }
}
