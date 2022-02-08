package service;

import db.MemoRepository;
import java.util.List;
import model.Memo;

public class MemoService {

    private final MemoRepository memoRepository;

    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }

    public void save(Memo memo) {
        memoRepository.save(memo);
    }

    public List<Memo> findAll() {
        return memoRepository.findAll();
    }
}
