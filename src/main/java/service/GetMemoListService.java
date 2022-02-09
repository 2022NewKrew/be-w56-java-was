package service;

import db.MemoStorage;
import java.util.ArrayList;
import model.Memo;

public class GetMemoListService {

    public static ArrayList<Memo> getMemoList() {
        return new ArrayList<>(MemoStorage.findAll());
    }
}
