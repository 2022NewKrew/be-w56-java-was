package framework.container;

import com.kakao.example.controller.UserController;
import org.modelmapper.ModelMapper;

public class Container {
    public UserController userController = new UserController();
    public ModelMapper modelMapper = new ModelMapper();
}
