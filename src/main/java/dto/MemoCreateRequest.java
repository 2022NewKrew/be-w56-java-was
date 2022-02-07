package dto;

import model.Memo;
import util.HttpRequestUtils;

import java.util.List;
import java.util.Map;

public class MemoCreateRequest {

    private final Long authorId;
    private final String content;

    private MemoCreateRequest(Map<String, List<String>> parameterMap, Long authorId) {
        validateParams(parameterMap, authorId);
        this.authorId = authorId;
        this.content = parameterMap.get("content").get(0);
    }

    public static MemoCreateRequest from(String requestBody, String auth) {
        Map<String, List<String>> parameterMap = HttpRequestUtils.parseQueryString(requestBody);
        long authorId = Long.parseLong(auth);
        return new MemoCreateRequest(parameterMap, authorId);
    }

    private void validateParams(Map<String, List<String>> parameterMap, Long authorId) {
        if (
                !parameterMap.containsKey("content") ||
                parameterMap.get("content").isEmpty() ||
                authorId == null) {
            throw new IllegalArgumentException("요청 파라미터가 존재하지 않습니다.");
        }
    }

    public String getContent() {
        return content;
    }

    public Memo toEntity() {
        return new Memo(authorId, content);
    }
}
