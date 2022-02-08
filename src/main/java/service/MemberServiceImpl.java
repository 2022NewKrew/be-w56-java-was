package service;

import db.DataBase;
import dto.UserCreateDto;
import model.User;
import repository.MemberRepository;
import repository.MemberRepositoryImpl;
import webserver.annotations.Autowired;
import webserver.annotations.Component;

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
}
