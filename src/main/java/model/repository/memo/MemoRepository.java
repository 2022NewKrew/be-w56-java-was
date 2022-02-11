package model.repository.memo;

import model.Memo;
import model.User;

import java.util.List;

public interface MemoRepository {
    Memo save(Memo memo);
    List<Memo> findAll();
}
