package repository;

import db.H2Repository;
import entity.UserEntity;

public class UserRepository extends H2Repository<UserEntity, String> {

    private static final UserRepository userRepository = new UserRepository(UserEntity.class);

    private UserRepository(Class<UserEntity> classType) {
        super(classType);
    }

    public static UserRepository getInstance() {
        return userRepository;
    }
}
