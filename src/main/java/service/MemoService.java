package service;

import db.MySQLConfig;
import model.Memo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MemoService {
    public static final MemoService INSTANCE = new MemoService();

    private final MySQLConfig mySQLConfig;
    private final UserService userService;

    private MemoService() {
        this.mySQLConfig = new MySQLConfig();
        this.userService = UserService.INSTANCE;
    }

    public void addMemo(Memo memo) throws SQLException {
        mySQLConfig.addMemo(memo);
    }

    public String getMemoList() throws SQLException {
        StringBuilder sb = new StringBuilder();
        ResultSet rs = mySQLConfig.getMemos();
        while (rs.next()) {
            sb.append("<li>\n<div class=\"wrap\">\n<div class=\"main\">\n<strong class=\"subject\">\n" +
                    "<a href=\"./qna/show.html\">" + rs.getString("content") + "</a>\n" +
                    "</strong>\n<div class=\"auth-info\">\n<i class=\"icon-add-comment\"></i>\n" +
                    "<span class=\"time\">" + rs.getString("createDate") + "</span>\n" +
                    "<a href=\"./user/profile.html\" class=\"author\">" + userService.getUserById(rs.getInt("author_id")).getName() + "</a>\n" +
                    "</div>\n</div>\n</div>\n</li>");
        }
        return sb.toString();
    }
}
