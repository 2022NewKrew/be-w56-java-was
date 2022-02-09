package adaptor.out.persistence.user;

import application.out.user.FindUserPort;
import application.out.user.UserDao;
import domain.user.User;

import java.util.List;
import java.util.Optional;

public class FindUserAdaptor implements FindUserPort {

    private final UserDao userDao;

    public FindUserAdaptor(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        return Optional.ofNullable(userDao.findByUserId(userId));
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }
}
