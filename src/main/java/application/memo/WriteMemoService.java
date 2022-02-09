package application.memo;

import application.exception.user.NonExistsUserIdException;
import application.in.memo.WriteMemoUseCase;
import application.out.memo.WriteMemoPort;
import application.out.user.FindUserPort;
import domain.memo.Memo;

public class WriteMemoService implements WriteMemoUseCase {

    private final WriteMemoPort writeMemoPort;
    private final FindUserPort findUserPort;

    public WriteMemoService(WriteMemoPort writeMemoPort, FindUserPort findUserPort) {
        this.writeMemoPort = writeMemoPort;
        this.findUserPort = findUserPort;
    }

    @Override
    public void write(String userId, Memo memo) {
        findUserPort.findByUserId(userId)
                .orElseThrow(NonExistsUserIdException::new);

        writeMemoPort.save(memo);
    }
}
