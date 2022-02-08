package repository;

import db.DataBase;
import model.User;
import webserver.annotations.Autowired;
import webserver.annotations.Component;

@Component
public class MemberRepositoryImpl implements MemberRepository {
    @Autowired
    public MemberRepositoryImpl() {}


    @Override
    public void insert(User user) {
        DataBase.addUser(user);
        System.out.println(user.getEmail());
    }
}
