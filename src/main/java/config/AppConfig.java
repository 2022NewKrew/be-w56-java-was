package config;

import repository.BoardRepository;
import repository.BoardRepositoryH2;
import repository.MemberRepository;
import repository.MemberRepositoryH2;

public class AppConfig {

    public MemberRepository memberRepository() {
        return new MemberRepositoryH2();
    }

    public BoardRepository boardRepository() {
        return new BoardRepositoryH2();
    }
}
