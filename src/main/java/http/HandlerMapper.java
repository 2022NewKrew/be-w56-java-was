package http;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import controller.Controller;
import controller.IndexController;
import controller.SignUpController;
import controller.UserFormController;

public class HandlerMapper {
    private final Table<String, String, Controller> controllerTable = HashBasedTable.create();

    private static final HandlerMapper handlerMapper = new HandlerMapper();

    private HandlerMapper(){
        controllerTable.put("GET", "/", new IndexController());
        controllerTable.put("GET", "/users/form", new UserFormController());
        controllerTable.put("POST", "/users", new SignUpController());
    }

    public static HandlerMapper getInstance(){
        return handlerMapper;
    }

    public Controller get(String method, String url){
        return controllerTable.get(method, url);
    }
}
