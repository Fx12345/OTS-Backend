package hello;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.*;
import usermanagement.UserAuthentificator;

@RestController
public class OtsServiceController {

    private final AtomicLong counter = new AtomicLong();

    @PostMapping("/user/login")
    User newUser(@RequestBody User newUser) {

        UserAuthentificator userAuthentificator = new UserAuthentificator();
        userAuthentificator.checkUser("Falk");
        System.out.println(newUser);
        return newUser;
    }

    @GetMapping("/users")
    public User user(@RequestParam(value="name", defaultValue = "World") String name) {
        return new User(1,name, "1234");
    }

}
