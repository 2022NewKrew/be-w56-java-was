package application.in.memo;

import domain.memo.Memo;

public interface WriteMemoUseCase {
    void write(String userId, Memo memo);
}
