package application.user;

import application.in.user.FindUserUseCase;
import application.out.user.FindUserPort;
import domain.user.User;

import java.util.List;

public class FindUserService implements FindUserUseCase {

    private final FindUserPort findUserPort;

    public FindUserService(FindUserPort findUserPort) {
        this.findUserPort = findUserPort;
    }

    @Override
    public List<User> findAll() {
        return findUserPort.findAll();
    }
}
