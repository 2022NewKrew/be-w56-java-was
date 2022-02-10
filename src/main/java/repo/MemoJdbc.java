package repo;

import jdbc.JedisPools;
import model.Memo;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemoJdbc {
    private static final String KEY_PREFIX = "MEMO::";
    private static final String KEY_INDEX = "MEMO_IDX";

    private final JedisPools jedisPools;

    private static long index;

    public MemoJdbc(final JedisPools jedisPools) {
        this.jedisPools = jedisPools;
        final Jedis jedis = jedisPools.get();
        jedis.incr(KEY_INDEX);
        index = jedis.decr(KEY_INDEX);
        jedis.close();
    }

    public void addMemo(final Memo memo) {
        final Jedis jedis = jedisPools.get();

        jedis.incr(KEY_INDEX);

        final JSONObject jsonMemo = new JSONObject();
        jsonMemo.put(Memo.KEY_CREATED_AT, memo.getCreatedAt());
        jsonMemo.put(Memo.KEY_USER_ID, memo.getUserId());
        jsonMemo.put(Memo.KEY_BODY, memo.getBody());
        jedis.set(KEY_PREFIX + index++, jsonMemo.toString());

        jedis.close();
    }

    public List<Memo> getAllOrderByCreatedAtDesc() {
        final Jedis jedis = jedisPools.get();
        final long maxIdx = Long.parseLong(jedis.get(KEY_INDEX), 10);
        final List<Memo> memos = new ArrayList<>();
        for (long l = maxIdx - 1; l >= 0; l--) {
            memos.add(new Memo(new JSONObject(jedis.get(KEY_PREFIX + l))));
        }
        jedis.close();

        return Collections.unmodifiableList(memos);
    }
}
