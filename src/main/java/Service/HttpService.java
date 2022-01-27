package Service;

import db.DataBase;
import model.User;

import java.util.Map;

public class HttpService {
    public void signup(Map<String, String> data) {
        User user = new User(data.get("userId"), data.get("password"), data.get("name"), data.get("email"));
        DataBase.addUser(user);

        // 데이터베이스에 잘 입력되었는지 확인
//        for (User u : DataBase.findAll()) {
//            System.out.println(u.toString());
//        }
    }

}
