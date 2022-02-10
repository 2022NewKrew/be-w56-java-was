package repository.user;

import model.user_account.UserAccount;
import model.user_account.UserAccountDTO;
import repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class UserAccountNoDbUseRepository implements Repository<UserAccount, UserAccountDTO, String> {
    private static final Map<String, UserAccount> USER_ID_USER_ACCOUNT_DB = new ConcurrentHashMap<>();
    private int id = 0;

    @Override
    public UserAccount save(UserAccountDTO userAccountDTO) {
        UserAccount userAccount = new UserAccount(userAccountDTO, id++);
        USER_ID_USER_ACCOUNT_DB.put(userAccount.getUserId(), userAccount);
        return userAccount;
    }

    @Override
    public Optional<UserAccount> findById(String userId) {
        return Optional.ofNullable(USER_ID_USER_ACCOUNT_DB.get(userId));
    }

    @Override
    public List<UserAccount> findAll() {
        return new ArrayList<>(USER_ID_USER_ACCOUNT_DB.values());
    }

    public void clearStore() {
        USER_ID_USER_ACCOUNT_DB.clear();
    }
}
