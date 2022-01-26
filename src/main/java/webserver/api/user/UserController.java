package webserver.api.user;

import webserver.dispatcher.dynamic.bind.annotation.*;

@RestController
public class UserController {

    @GetMapping("/join")
    public String join() {
        return "hello world";
    }

    @PostMapping
    public String joinPost() {
        return "joinPost";
    }

    @PutMapping
    public String affa() {
        return "";
    }

    @DeleteMapping
    public String asffasfaf() {
        return "";
    }
}
