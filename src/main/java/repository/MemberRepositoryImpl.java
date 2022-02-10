package repository;

import db.DataBase;
import model.User;
import webserver.annotations.Autowired;
import webserver.annotations.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MemberRepositoryImpl implements MemberRepository {
    @Autowired
    public MemberRepositoryImpl() {}


    @Override
    public void insert(User user) {
        DataBase.addUser(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(DataBase.findUserByUserId(username));
    }

    @Override
    public List<User> findAll() {
        return (List<User>) DataBase.findAllUser();
    }
}
