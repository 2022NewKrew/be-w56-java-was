package application.memo;

import application.exception.memo.NonExistsMemoIdException;
import application.in.memo.ReadMemoUseCase;
import application.out.memo.ReadMemoPort;
import domain.memo.Memo;

import java.util.List;

public class ReadMemoService implements ReadMemoUseCase {

    private final ReadMemoPort readMemoPort;

    public ReadMemoService(ReadMemoPort readMemoPort) {
        this.readMemoPort = readMemoPort;
    }

    @Override
    public Memo readMemo(int id) {
        return readMemoPort.findOneById(id)
                .orElseThrow(NonExistsMemoIdException::new);
    }

    @Override
    public List<Memo> readAllMemo() {
        return readMemoPort.findAll();
    }
}
