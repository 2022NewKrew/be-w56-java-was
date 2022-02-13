package model.repository.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.User;

public class UserRepositoryMap implements UserRepository{
    private final Map<Integer,User> idIndex = new HashMap<>();
    private final Map<String,User> stringIdIndex = new HashMap<>();
    private int maxIndex = 1;

    @Override
    public User save(User user){
        if (user.isNew()) {
            int id = insert(user);
            return User.builder()
                    .id(id)
                    .stringId(user.getStringId())
                    .name(user.getName())
                    .password(user.getPassword())
                    .email(user.getEmail())
                    .build();
        }
        update(user);
        return user;
    }

    @Override
    public User findById(int id){
        return idIndex.getOrDefault(id, null);
    }

    @Override
    public User findByStringId(String stringId){
        return stringIdIndex.getOrDefault(stringId, null);
    }

    @Override
    public List<User> findAll(){
        return new ArrayList<>(idIndex.values());
    }

    private int insert(User user){
        int id = maxIndex;
        User userToInsert = User.builder()
                .id(id)
                .stringId(user.getStringId())
                .name(user.getName())
                .password(user.getPassword())
                .email(user.getEmail())
                .build();
        idIndex.put(userToInsert.getId(), userToInsert);
        stringIdIndex.put(userToInsert.getStringId(), userToInsert);
        maxIndex++;
        return id;
    }

    private void update(User user){
        User userToUpdate = findById(user.getId());
        userToUpdate.changeName(user.getName());
        userToUpdate.changePassword(user.getPassword());
        userToUpdate.changeEmail(user.getEmail());
    }
}
