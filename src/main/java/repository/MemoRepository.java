package repository;

import model.Memo;

import java.util.List;

public interface MemoRepository {
    void insert(Memo memo);

    List<Memo> findAll();
}
