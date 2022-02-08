package com.kakao.example.model.repository;

import com.kakao.example.model.domain.Memo;
import com.kakao.example.util.DbUtil;
import com.kakao.example.util.exception.MemoNotFoundException;
import framework.util.annotation.Autowired;
import framework.util.annotation.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static framework.util.annotation.Component.ComponentType.REPOSITORY;

@Component(type = REPOSITORY)
public class MemoRepositoryJdbcImpl implements MemoRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemoRepositoryJdbcImpl.class);

    private final DataSource dataSource;

    @Autowired
    public MemoRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Memo> addMemo(Memo memo) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();

            String sql = "INSERT INTO MEMOS (WRITER_ID, CONTENT, CREATED_DATE) \n"
                    + "VALUES (?, ?, NOW())";
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, memo.getWriterId());
            pstmt.setString(2, memo.getContent());

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                long id = rs.getLong(1);
                rs.close();
                pstmt.close();

                sql = "SELECT CREATED_DATE \n"
                        + "FROM MEMOS \n"
                        + "WHERE MEMO_ID = ?";

                pstmt = conn.prepareStatement(sql);
                pstmt.setLong(1, id);

                rs = pstmt.executeQuery();

                if (rs.next()) {
                    memo.setMemoId(id);
                    memo.setCreatedDate(rs.getTimestamp(1).toLocalDateTime());
                    memo.setFormattedCreatedDate(rs.getTimestamp(1).toLocalDateTime()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                } else {
                    throw new MemoNotFoundException();
                }
            } else {
                throw new MemoNotFoundException();
            }
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
        } finally {
            DbUtil.close(rs, pstmt, conn);
        }

        return Optional.of(memo);
    }

    @Override
    public List<Memo> findAll() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Memo> memoList = new ArrayList<>();

        try {
            conn = dataSource.getConnection();

            String sql = "SELECT MEMO_ID, WRITER_ID, CONTENT, CREATED_DATE \n"
                    + "FROM MEMOS \n"
                    + "ORDER BY CREATED_DATE DESC";
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                memoList.add(Memo.builder()
                        .memoId(rs.getInt(1))
                        .writerId(rs.getString(2))
                        .content(rs.getString(3))
                        .createdDate(rs.getTimestamp(4).toLocalDateTime())
                        .formattedCreatedDate(rs.getTimestamp(4).toLocalDateTime()
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .build());
            }
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
        } finally {
            DbUtil.close(rs, pstmt, conn);
        }

        return memoList;
    }
}
