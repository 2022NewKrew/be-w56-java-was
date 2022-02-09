package service;

import dto.UserCreateDto;
import dto.UserItemDto;
import dto.UserSignInDto;
import exception.EntityNotFoundException;
import model.User;
import repository.MemberRepository;
import webserver.annotations.Autowired;
import webserver.annotations.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MemberServiceImpl implements MemberService {
    private MemberRepository memberRepository;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void create(UserCreateDto userCreateDto) {
        User user = User.of(userCreateDto);
        memberRepository.insert(user);
    }

    @Override
    public User signIn(UserSignInDto userSignInDto) {
        return memberRepository.findByUsername(userSignInDto.getUsername())
                .filter(user_ -> user_.getPassword().equals(userSignInDto.getPassword()))
                .orElseThrow(() -> new EntityNotFoundException("member not found"));
    }

    @Override
    public List<UserItemDto> getList() {
        return memberRepository.findAll()
                .stream()
                .map(UserItemDto::of)
                .collect(Collectors.toList());
    }
}
