package repository;

import model.Member;

import java.sql.SQLException;
import java.util.List;

public interface MemberRepository {

    Long save(Member member) throws SQLException;

    List<Member> findAll() throws SQLException;

    Member findByUserIdAndPassword(String userId, String password) throws SQLException;
}
