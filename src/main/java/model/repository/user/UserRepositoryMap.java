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
        if(user.isNew()) {
            insert(user);
            return user;
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

    private void insert(User user){
        User userToInsert = User.builder()
                .id(maxIndex)
                .stringId(user.getStringId())
                .name(user.getName())
                .password(user.getPassword())
                .email(user.getEmail())
                .build();
        idIndex.put(userToInsert.getId(), userToInsert);
        stringIdIndex.put(userToInsert.getStringId(), userToInsert);
        maxIndex++;
    }

    private void update(User user){
        User userToUpdate = findById(user.getId());
        userToUpdate.changeName(user.getName());
        userToUpdate.changePassword(user.getPassword());
        userToUpdate.changeEmail(user.getEmail());
    }
}
