package adaptor.out.persistence.user;

import application.out.SignUpUserPort;
import application.out.UserDao;
import domain.user.User;

public class SignUpUserInMemoryAdaptor implements SignUpUserPort {

    private static final SignUpUserInMemoryAdaptor INSTANCE = new SignUpUserInMemoryAdaptor();
    private final UserDao userDao = UserInMemoryDao.getINSTANCE();

    public static SignUpUserInMemoryAdaptor getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public void addUser(User user) {
        userDao.save(user);
    }
}
