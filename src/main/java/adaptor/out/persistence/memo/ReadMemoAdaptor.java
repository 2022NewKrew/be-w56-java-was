package adaptor.out.persistence.memo;

import application.out.memo.MemoDao;
import application.out.memo.ReadMemoPort;
import domain.memo.Memo;

import java.util.List;
import java.util.Optional;

public class ReadMemoAdaptor implements ReadMemoPort {

    private final MemoDao memoDao;

    public ReadMemoAdaptor(MemoDao memoDao) {
        this.memoDao = memoDao;
    }

    @Override
    public Optional<Memo> findOneById(int id) {
        return Optional.empty();
    }

    @Override
    public List<Memo> findAll() {
        return null;
    }
}
