package application.out.memo;

import domain.memo.Memo;

import java.util.List;

public interface MemoDao {
    void save(Memo memo);
    Memo findOneById(int id);
    List<Memo> findAll();
}
