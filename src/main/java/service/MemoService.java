package service;

import dto.MemoCreateDto;
import dto.MemoResponseDto;
import dto.mapper.MemoMapper;
import model.repository.memo.MemoRepository;
import model.repository.memo.MemoRepositoryJdbc;

import java.util.List;

public class MemoService {
    private static final MemoService instance = new MemoService();

    private MemoService() {}

    public static MemoService getInstance() {
        return instance;
    }

    private static final MemoRepository memoRepository = new MemoRepositoryJdbc();

    public void create(MemoCreateDto memoCreateDto){
        memoRepository.save(MemoMapper.INSTANCE.toEntityFromSaveDto(memoCreateDto));
    }

    public List<MemoResponseDto> findAll(){
        System.out.println(memoRepository.findAll().toString());
        return MemoMapper.INSTANCE.toDtoList(memoRepository.findAll());
    }
}
