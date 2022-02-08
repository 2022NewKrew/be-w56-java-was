package repository;

import db.H2EntityManagerFactory;
import entity.MemoEntity;
import entity.UserEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.Persistence;
import java.time.LocalDateTime;

class MemoRepositoryTest {

    private final UserRepository userRepository = UserRepository.getInstance();
    private final MemoRepository memoRepository = MemoRepository.getInstance();

    @BeforeAll
    static void init() {
        H2EntityManagerFactory.emf = Persistence.createEntityManagerFactory("java-was");

    }

    @Test
    void crud() {
        UserEntity userEntity = new UserEntity("userId", "password", "name", "email", LocalDateTime.now());
        userRepository.save(userEntity);
        MemoEntity memoEntity = new MemoEntity("name", "contents", LocalDateTime.now(), userEntity);
        memoRepository.save(memoEntity);
        MemoEntity find = memoRepository.findById(1L);
        System.out.println(find);
    }

}
