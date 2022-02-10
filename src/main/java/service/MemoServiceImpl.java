package service;

import dto.memo.MemoCreateDto;
import dto.memo.MemoItemDto;
import exception.EntityNotFoundException;
import model.Memo;
import model.User;
import repository.MemberRepository;
import repository.MemoRepository;
import webserver.annotations.Autowired;
import webserver.annotations.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MemoServiceImpl implements MemoService {
    private final MemoRepository memoRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public MemoServiceImpl(MemoRepository memoRepository,
                           MemberRepository memberRepository) {
        this.memoRepository = memoRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public List<MemoItemDto> findAll() {
        return memoRepository.findAll()
                .stream()
                .map(MemoItemDto::of)
                .collect(Collectors.toList());
    }

    @Override
    public void create(MemoCreateDto memoCreateDto, String sessionedUserId) {
        User writer = memberRepository.findByUsername(sessionedUserId)
                .orElseThrow(() -> new EntityNotFoundException("member not found"));

        Memo memo = Memo.of(memoCreateDto, writer);
        memoRepository.insert(memo);
    }
}
