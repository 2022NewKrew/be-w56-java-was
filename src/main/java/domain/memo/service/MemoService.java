package domain.memo.service;

import domain.memo.dto.MemoCreate;
import domain.memo.dto.MemoInfo;
import domain.memo.model.Memo;
import domain.memo.repository.InMemoryMemoRepository;
import domain.memo.repository.MemoRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class MemoService {

    private final MemoRepository memoRepository;

    public static MemoService create() {
        return new MemoService(InMemoryMemoRepository.get());
    }

    private MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }

    public void create(MemoCreate memoCreate) {
        Memo memo = Memo.builder()
            .createdAt(LocalDate.now())
            .author(memoCreate.getAuthor())
            .content(memoCreate.getContent())
            .build();
        memoRepository.save(memo);
    }

    public List<MemoInfo> readAll() {
        return memoRepository.findAll().stream()
            .map(MemoInfo::from)
            .collect(Collectors.toUnmodifiableList());
    }
}
