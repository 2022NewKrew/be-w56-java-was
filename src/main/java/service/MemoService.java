package service;

import model.memo.Memo;
import model.memo.MemoDTO;
import model.user_account.UserAccount;
import model.user_account.UserAccountDTO;
import repository.Repository;
import repository.memo.MemoNoDbUseRepository;
import repository.user.UserAccountNoDbUseRepository;

import java.util.List;
import java.util.Optional;

public class MemoService {
    private final Repository<Memo, MemoDTO, Integer> memoRepository;

    public MemoService(){
        memoRepository = new MemoNoDbUseRepository();
    }

    public MemoService(Repository<Memo, MemoDTO, Integer> memoRepository){
        this.memoRepository = memoRepository;
    }

    public String join(MemoDTO memoDTO){
        Memo savedMemo = memoRepository.save(memoDTO);

        return savedMemo.getId();
    }

    public List<Memo> findAll(){
        return memoRepository.findAll();
    }

    public Optional<Memo> findOne(int id){
        return memoRepository.findById(id);
    }
}
