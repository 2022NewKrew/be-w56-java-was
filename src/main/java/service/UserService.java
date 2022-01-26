package service;

import db.RepositoryImpl;
import model.Request;

public class UserService {
    private UserService() {
        throw new IllegalStateException("Utility class");
    }
    
    public static void save(Request request) {
        RepositoryImpl.save(request);
    }

    public static boolean isRightLogin(Request request) {
        return RepositoryImpl.isRightLogin(request);
    }
}
