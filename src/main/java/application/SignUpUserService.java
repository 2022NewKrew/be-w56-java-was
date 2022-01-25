package application;

import adaptor.out.persistence.user.SignUpUserInMemoryAdaptor;
import application.in.SignUpUserUseCase;
import application.out.SignUpUserPort;
import domain.user.User;

public class SignUpUserService implements SignUpUserUseCase {

    private static final SignUpUserService INSTANCE = new SignUpUserService();
    private final SignUpUserPort signUpUserPort = SignUpUserInMemoryAdaptor.getINSTANCE();

    public static SignUpUserService getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public void signUp(User user) {
        signUpUserPort.addUser(user);
    }
}
