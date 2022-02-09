package service;

import static org.assertj.core.api.Assertions.assertThat;

import db.MemoStorage;
import java.time.LocalDateTime;
import java.util.List;
import model.Memo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class GetMemoListServiceTest {

    private static final Memo[] memos = {
        new Memo("champ", "Hello World", LocalDateTime.now()),
        new Memo("kakao", "Hello Kakao", LocalDateTime.now())
    };

    @BeforeAll
    static void setup() {
        for (Memo memo : memos) {
            MemoStorage.addMemo(memo);
        }
    }

    @AfterAll
    static void close() {
        MemoStorage.clear();
    }

    @Test
    void getMemoList() {
        List<Memo> memoList = GetMemoListService.getMemoList();
        for (Memo memo : memoList) {
            assertThat(memo).isIn((Object[]) memos);
        }
    }
}
