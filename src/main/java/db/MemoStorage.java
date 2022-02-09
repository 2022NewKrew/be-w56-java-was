package db;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import model.Memo;

public class MemoStorage {

    private static final Map<Long, Memo> memos = Maps.newHashMap();
    private static final AtomicLong memoId = new AtomicLong();

    public static void addMemo(Memo memo) {
        memo.setId(memoId.getAndIncrement());
        memos.put(memo.getId(), memo);
    }

    public static Collection<Memo> findAll() {
        return memos.values();
    }

    public static void clear() {
        memos.clear();
    }
}
