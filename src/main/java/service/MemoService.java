package service;

import dto.MemoDto;
import mapper.MemoMapper;
import repository.MemoRepository;

import java.util.List;
import java.util.stream.Collectors;

public class MemoService {

    private static final MemoService memoService = new MemoService();
    private final MemoRepository memoRepository = MemoRepository.getInstance();

    private MemoService() {
    }

    public static MemoService getInstance() {
        return memoService;
    }

    public void save(MemoDto memoDto) {
        memoRepository.save(MemoMapper.toMemoEntity(memoDto));
    }

    public List<MemoDto> findAll() {
        return memoRepository.findAll().stream()
                .map(MemoMapper::toMemoDto)
                .collect(Collectors.toList());
    }
}
