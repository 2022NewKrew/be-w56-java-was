package application.in.memo;

import domain.memo.Memo;

import java.util.List;

public interface ReadMemoUseCase {
    Memo readMemo(int id);
    List<Memo> readAllMemo();
}
