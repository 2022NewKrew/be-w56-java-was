package repository.memo;

import model.memo.Memo;
import model.memo.MemoDTO;
import model.user_account.UserAccount;
import model.user_account.UserAccountDTO;
import repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class MemoNoDbUseRepository implements Repository<Memo, MemoDTO, Integer> {
    private static final Map<Integer, Memo> ID_MEMO_DB = new ConcurrentHashMap<>();
    private int id = 0;

    @Override
    public Memo save(MemoDTO memoDTO) {
        Memo memo = new Memo(memoDTO, id++);

        ID_MEMO_DB.put(Integer.parseInt(memo.getId()), memo);

        return memo;
    }

    @Override
    public Optional<Memo> findById(Integer id) {
        return Optional.ofNullable(ID_MEMO_DB.get(id));
    }

    @Override
    public List<Memo> findAll() {
        return new ArrayList<>(ID_MEMO_DB.values());
    }
}
