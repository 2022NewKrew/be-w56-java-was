package model.repository.memo;

import model.Memo;
import java.util.List;

public interface MemoRepository {
    Memo save(Memo memo);
    List<Memo> findAll();
}
