package service;

import dao.MemoDAO;
import model.Memo;
import webserver.http.HttpRequest;

import java.sql.SQLException;
import java.util.List;

public class MemoService {
    private final MemoDAO memoDAO;

    public MemoService() { this.memoDAO = new MemoDAO(); }

    public void store(HttpRequest httpRequest) throws SQLException {
        memoDAO.storeMemo(httpRequest);
    }

    public List<Memo> findAll() throws SQLException {
        return memoDAO.findAllMemos();
    }
}
