package adaptor.out.persistence.user;

import application.out.SignUpUserPort;
import application.out.UserDao;
import domain.user.User;

public class SignUpUserInMemoryAdaptor implements SignUpUserPort {

    private final UserDao userDao;

    public SignUpUserInMemoryAdaptor(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void addUser(User user) {
        userDao.save(user);
    }
}
