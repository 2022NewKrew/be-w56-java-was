package domain.memo.repository;

import domain.memo.model.Memo;
import java.util.Collection;

public interface MemoRepository {

    void save(Memo memo);

    Collection<Memo> findAll();
}
