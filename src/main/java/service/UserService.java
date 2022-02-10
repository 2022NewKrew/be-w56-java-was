package service;

import model.user_account.UserAccount;
import model.user_account.UserAccountDTO;
import repository.Repository;
import repository.user.UserAccountNoDbUseRepository;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final Repository<UserAccount, UserAccountDTO, String> userRepository;

    public UserService(){
        userRepository = new UserAccountNoDbUseRepository();
    }

    public UserService(Repository<UserAccount, UserAccountDTO, String> userRepository){
        this.userRepository = userRepository;
    }

    public String join(UserAccountDTO userAccountDTO) throws IllegalStateException{
        validateDuplicateUserId(userAccountDTO);

        userRepository.save(userAccountDTO);

        return userAccountDTO.getUserId();
    }

    private void validateDuplicateUserId(UserAccountDTO userAccountDTO) throws IllegalStateException{
        userRepository.findById(userAccountDTO.getUserId())
                .ifPresent(m -> {
                    throw new IllegalStateException("중복된 userId로 가입 요청을 하였습니다.");
                });
    }

    public List<UserAccount> findAll(){
        return userRepository.findAll();
    }

    public Optional<UserAccount> findOne(String userId){
        return userRepository.findById(userId);
    }

    public boolean isPasswordEqual(UserAccount userAccount, String userInputPassword){
        return userInputPassword.equals(userAccount.getPassword());
    }
}
