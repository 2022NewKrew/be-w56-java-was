package service;

import model.UserAccount;
import model.UserAccountDTO;
import repository.Repository;
import repository.user.UserAccountNoDbUseRepository;

public class UserService {
    private final Repository<UserAccount, UserAccountDTO, String> userRepository;

    public UserService(){
        userRepository = new UserAccountNoDbUseRepository();
    }

    public String join(UserAccountDTO userAccountDTO){
        userRepository.save(userAccountDTO);

        return userAccountDTO.getUserId();
    }
}
