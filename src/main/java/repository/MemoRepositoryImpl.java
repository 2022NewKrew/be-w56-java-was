package repository;

import db.DataBase;
import model.Memo;
import webserver.annotations.Autowired;
import webserver.annotations.Component;

import java.util.List;

@Component
public class MemoRepositoryImpl implements MemoRepository {
    @Autowired
    public MemoRepositoryImpl() {
    }

    @Override
    public void insert(Memo memo) {
        DataBase.addMemo(memo);
    }

    @Override
    public List<Memo> findAll() {
        return DataBase.findAllMemo();
    }
}
