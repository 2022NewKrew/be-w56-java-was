package service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import db.MemoStorage;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.Memo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class GetMemoListServiceTest {

    private final Memo[] memos = {
        new Memo("champ", "Hello World", LocalDateTime.now()),
        new Memo("kakao", "Hello Kakao", LocalDateTime.now())
    };

    private static MockedStatic<MemoStorage> mMemoStorage;

    @BeforeAll
    public static void setup() {
        mMemoStorage = mockStatic(MemoStorage.class);
    }

    @AfterAll
    public static void close() {
        mMemoStorage.close();
    }

    @Test
    void getMemoList() throws SQLException, ClassNotFoundException {

        when(MemoStorage.findAll()).thenReturn(new ArrayList<>(List.of(memos)));

        List<Memo> memoList = GetMemoListService.getMemoList();
        for (Memo memo : memoList) {
            assertThat(memo).isIn((Object[]) memos);
        }
    }
}
