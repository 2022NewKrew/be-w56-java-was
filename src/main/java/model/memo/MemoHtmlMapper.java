package model.memo;

import model.Mapper;
import model.user_account.UserAccount;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MemoHtmlMapper implements Mapper {
    private final Map<String, Supplier<String>> map = new HashMap<>();

    public MemoHtmlMapper(Memo memo) {
        map.put("id", memo::getId);
        map.put("writer", memo::getWriter);
        map.put("date", memo::getDate);
        map.put("contents", memo::getContents);
    }

    @Override
    public Map<String, Supplier<String>> getMap() {
        return map;
    }
}
