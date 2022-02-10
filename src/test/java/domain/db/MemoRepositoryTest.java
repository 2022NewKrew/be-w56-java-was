package domain.db;

import domain.model.Memo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemoRepositoryTest {

    @Test
    void insert_test() {
        // given
        Memo memo = new Memo();
        memo.setContent("sdfa");
        memo.setAuthor("fsad");

        // when

        new MemoRepository().create(memo);

        // then
    }

}
