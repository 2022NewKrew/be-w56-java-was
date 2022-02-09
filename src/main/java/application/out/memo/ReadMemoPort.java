package application.out.memo;

import domain.memo.Memo;

import java.util.List;
import java.util.Optional;

public interface ReadMemoPort {
    Optional<Memo> findOneById(int id);
    List<Memo> findAll();
}
