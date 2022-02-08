package service;

import dto.MemoDto;
import dto.UserDto;
import exception.UserException;
import model.Memo;
import model.User;
import repository.MemoRepository;

import java.util.List;
import java.util.stream.Collectors;

public class MemoService {
    private static final MemoService INSTANCE = new MemoService();

    public static MemoService getInstance() {
        return INSTANCE;
    }

    private final MemoRepository memoRepository;

    private MemoService() {
        memoRepository = MemoRepository.getInstance();
    }

    public void register(MemoDto dto) {
        memoRepository.save(dtoToEntity(dto));
    }

    public List<MemoDto> getAllMemo() {
        return memoRepository.findAll().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    private Memo dtoToEntity(MemoDto dto) {
        return Memo.builder()
                .memoId(dto.getMemoId())
                .writer(User.builder().userId(dto.getWriterId()).build())
                .content(dto.getContent())
                .build();
    }

    private MemoDto entityToDto(Memo entity) {
        return MemoDto.builder()
                .memoId(entity.getMemoId())
                .writerId(entity.getWriter().getUserId())
                .writerName(entity.getWriter().getName())
                .content(entity.getContent())
                .regDate(entity.getRegDate())
                .build();
    }
}
