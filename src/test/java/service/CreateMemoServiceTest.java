package service;

import static org.assertj.core.api.Assertions.assertThat;

import db.MemoStorage;
import java.util.ArrayList;
import java.util.List;
import model.CreateMemoRequest;
import model.Memo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

class CreateMemoServiceTest {

    @AfterAll
    static void close() {
        MemoStorage.clear();
    }

    @Test
    void createMemo() {
        // given
        String writer = "champ";
        String content = "Hello World";
        CreateMemoRequest createMemoRequest = new CreateMemoRequest(writer, content);

        // when
        CreateMemoService.createMemo(createMemoRequest);

        // then
        List<Memo> memoList = new ArrayList<>(MemoStorage.findAll());
        assertThat(
            memoList.stream()
                    .anyMatch(m -> m.getWriter().equals(writer) && m.getContent().equals(content))
        ).isTrue();
    }
}
