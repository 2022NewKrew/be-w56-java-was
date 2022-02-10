package adaptor.out.persistence.memo;

import application.out.memo.MemoDao;
import application.out.memo.WriteMemoPort;
import domain.memo.Memo;

public class WriteMemoAdaptor implements WriteMemoPort {

    private final MemoDao memoDao;

    public WriteMemoAdaptor(MemoDao memoDao) {
        this.memoDao = memoDao;
    }

    @Override
    public void save(Memo memo) {
        memoDao.save(memo);
    }
}
