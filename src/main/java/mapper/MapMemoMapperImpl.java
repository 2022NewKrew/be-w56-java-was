package mapper;

import model.Memo;

import javax.annotation.processing.Generated;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Generated(value = "org.mapstruct.ap.MappingProcessor")
public class MapMemoMapperImpl implements MapMemoMapper {

    @Override
    public Memo toRightObject(Map<String, String> map) {
        if (map == null) {
            return null;
        }

        Memo memo = new Memo();

        memo.setName(map.get("writer"));
        memo.setContent(map.get("contents"));
        memo.setCreatedAt(LocalDateTime.now());

        return memo;
    }

    @Override
    public Map<String, String> toLeftObject(Memo memo) {
        if (memo == null) {
            return null;
        }

        Map<String, String> map = new HashMap<>();

        map.put("writer", memo.getName());
        map.put("contents", memo.getContent());
        map.put("createdAt", memo.getCreatedAt().toString());

        return map;
    }
}
