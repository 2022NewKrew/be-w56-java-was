package app.user.application;

import app.user.application.port.in.ListUserUseCase;
import app.user.application.port.out.LoadUserPort;
import app.user.domain.User;
import java.util.Collection;

public class ListUserService implements ListUserUseCase {

    private final LoadUserPort loadUserPort;

    public ListUserService(LoadUserPort loadUserPort) {
        this.loadUserPort = loadUserPort;
    }

    @Override
    public Collection<User> listAll() {
        return loadUserPort.findAll();
    }
}
