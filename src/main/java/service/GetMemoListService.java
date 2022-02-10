package service;

import db.MemoStorage;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Memo;

public class GetMemoListService {

    public static ArrayList<Memo> getMemoList() throws SQLException, ClassNotFoundException {
        return new ArrayList<>(MemoStorage.findAll());
    }
}
