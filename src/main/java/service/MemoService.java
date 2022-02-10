package service;

import model.Memo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.InMemoryMemoRepository;

public class MemoService {
    private static final Logger log = LoggerFactory.getLogger(MemoService.class);

    private static final InMemoryMemoRepository memoRepository = InMemoryMemoRepository.getInstance();

    public static void create(Memo memo) {
        memoRepository.addMemo(memo);
        log.debug("Memo DataBase Status: {}", memoRepository.findAll());
    }

}
