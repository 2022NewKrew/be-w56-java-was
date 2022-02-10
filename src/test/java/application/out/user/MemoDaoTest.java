package application.out.user;

import adaptor.out.persistence.mysql.QueryBuilder;
import adaptor.out.persistence.mysql.memo.MemoMysqlDao;
import application.out.memo.MemoDao;
import domain.memo.Memo;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class MemoDaoTest {

    MemoDao memoDao = new MemoMysqlDao(new QueryBuilder("memo", List.of("id", "writer", "content", "created_at")));

    @Test
    @Disabled
    void save() {
        Memo memo = Memo.builder()
                .id(1)
                .writer("binary.yun")
                .createdAt(LocalDateTime.now())
                .content("Hello")
                .build();
        memoDao.save(memo);
    }

    @Test
    void findOneById() {
        Memo memo = memoDao.findOneById(1);
        assertThat(1)
                .isNotNull();
        assertThat(memo.getWriter())
                .isEqualTo("binary.yun");
    }

    @Test
    void findAll() {
        List<Memo> all = memoDao.findAll();

        assertThat(all.size()).isEqualTo(1);
    }
}
