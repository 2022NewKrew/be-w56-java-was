package db;

import com.mongodb.client.result.InsertOneResult;
import model.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by melodist
 * Date: 2022-02-08 008
 * Time: 오후 8:42
 */
class MongoDbTest {

    @Test
    public void addUserTest(){
        // given
        User user = new User("test", "test", "test", "test");

        // when
        InsertOneResult result = MongoDb.addUser(user);

        // then
        assertThat(result.wasAcknowledged()).isTrue();
    }

    @Test
    public void findAllTest(){
        // given
        User user = new User("test", "test", "test", "test");
        MongoDb.addUser(user);

        // when
        List<User> users = MongoDb.findAll();

        // then
        assertThat(users).anyMatch(u -> u.getUserId().equals("test"));
        assertThat(users).anyMatch(u -> u.getPassword().equals("test"));
        assertThat(users).anyMatch(u -> u.getName().equals("test"));
        assertThat(users).anyMatch(u -> u.getEmail().equals("test"));
    }



}
