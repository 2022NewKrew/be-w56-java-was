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

        validateDuplicateUserId(userAccountDTO);

        return userAccountDTO.getUserId();
    }

    private void validateDuplicateUserId(UserAccountDTO userAccountDTO) throws IllegalStateException{
        userRepository.findById(userAccountDTO.getUserId())
                .ifPresent(m -> {
                    throw new IllegalStateException("중복된 userId로 가입 요청을 하였습니다.");
                });
    }
}
