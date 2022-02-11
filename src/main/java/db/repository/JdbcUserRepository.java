package db.repository;

import db.mapper.UserRowMapper;
import db.util.MyJdbcTemplate;
import webserver.domain.entity.User;
import webserver.domain.repository.UserRepository;

import java.util.List;
import java.util.Optional;

public class JdbcUserRepository implements UserRepository {
    private static final UserRowMapper userRowMapper = new UserRowMapper();

    @Override
    public List<User> getAll() {
        return MyJdbcTemplate.query("select * from member", userRowMapper);
    }

    @Override
    public Optional<User> getBy(String id) {
        List<User> users = MyJdbcTemplate.query(String.format("select * from member where userId = '%s'",id), userRowMapper);

        return users.isEmpty()
                ? Optional.empty()
                : Optional.of(users.get(0));
    }

    @Override
    public void save(User user) {
        if(getBy(user.getUserId()).isPresent()){
            throw new IllegalStateException("이미 사용자가 있어서 저장하지 못합니다.");
        }

        MyJdbcTemplate.update("insert into member(userId, password, name, email) values(?,?,?,?)"
                , user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    @Override
    public void saveAll(List<User> users) {
        for (User user : users) {
            save(user);
        }
    }

    @Override
    public void delete(String userId) {
        final String queryString = String.format("delete from MEMBER where userId='%s'", userId);
        MyJdbcTemplate.update(queryString);
    }

    @Override
    public void deleteAll() {
        final String queryString = "delete from MEMBER";
        MyJdbcTemplate.update(queryString);
    }
}
