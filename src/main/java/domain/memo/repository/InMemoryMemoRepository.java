package domain.memo.repository;

import domain.memo.model.Memo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class InMemoryMemoRepository implements MemoRepository {

    private static InMemoryMemoRepository instance;

    private final List<Memo> memos;

    public static InMemoryMemoRepository get() {
        if (instance == null) {
            instance = new InMemoryMemoRepository(new ArrayList<>());
        }
        return instance;
    }

    private InMemoryMemoRepository(List<Memo> memos) {
        this.memos = memos;
    }

    @Override
    public void save(Memo memo) {
        memos.add(0, memo);
    }

    @Override
    public Collection<Memo> findAll() {
        return Collections.unmodifiableList(memos);
    }
}
