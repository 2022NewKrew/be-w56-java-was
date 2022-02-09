package service;

import dto.memo.MemoItemDto;
import repository.MemoRepository;
import webserver.annotations.Autowired;
import webserver.annotations.Component;

import java.util.List;

@Component
public class MemoServiceImpl implements MemoService {
    private final MemoRepository memoRepository;

    @Autowired
    public MemoServiceImpl(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }

    @Override
    public List<MemoItemDto> findAll() {
        return null;
    }
}
