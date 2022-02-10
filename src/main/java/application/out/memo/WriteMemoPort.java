package application.out.memo;

import domain.memo.Memo;

public interface WriteMemoPort {
    void save(Memo memo);
}
