package controller;

import dto.ControllerDTO;
import http.HttpMethod;
import model.User;
import user.controller.UserController;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class ControllerManager {
    private Function<ControllerDTO, String> function;
    private ControllerDTO controllerDTO;

    public ControllerManager(String path, HttpMethod method, Map<String, String> elements){
        controllerDTO = new ControllerDTO(path, elements);

        //mapping to method
        if(path.equals("/user/create")) {
            function = new UserController()::createUser; //createUser만 메모리에 올릴 수 있는 방법이 없을까...
        }
    }

    public String execute(){
        if(function == null) {
            return controllerDTO.getPath();
        }

        return function.apply(controllerDTO);
    }

}
