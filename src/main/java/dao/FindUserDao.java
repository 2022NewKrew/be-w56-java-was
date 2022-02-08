package dao;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

/**
 * Created by melodist
 * Date: 2022-02-08 008
 * Time: 오후 9:18
 */
public class FindUserDao {
    private ObjectId id;

    @BsonProperty("user")
    private UserDao userDao;

    public FindUserDao() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
