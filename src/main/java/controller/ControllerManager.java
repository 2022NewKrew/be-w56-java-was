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

        //@GetMapping(value = "/user/create") 과 동일
        if(path.equals("/user/create") && method.equals(HttpMethod.GET)) {
            function = new UserController()::createUser; //createUser만 메모리에 올릴 수 있는 방법이 없을까...
        }
        else if(path.equals("/user/create") && method.equals(HttpMethod.POST)) {
            function = new UserController()::createUser;
        }

    }

    public String execute(){
        //메소드와 매핑이 되어있지 않은 메소드인경우 요청한 path그대로 정적페이지를 요청한다.
        if(function == null) {
            return controllerDTO.getPath();
        }

        return function.apply(controllerDTO);
    }

}
