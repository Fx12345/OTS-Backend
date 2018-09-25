package hello;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.*;
import usermanagement.UserAuthentificator;

@RestController
public class OtsServiceController {

    private final AtomicLong counter = new AtomicLong();
    UserAuthentificator userAuthentificator = new UserAuthentificator();

    @PostMapping("/user/login")
    User checkUser(@RequestBody User user) {

      if(userAuthentificator.checkUser(user.getName(),  user.getPassword()) == 0) {
          System.out.println("User checked");
          return user;
      }
      if(userAuthentificator.checkUser(user.getName(),  user.getPassword()) == 1) {
            return new User(-1,"wrong password", "-");
      } else {
          return new User(-2, "no User found", "-");
      }
    }

    @PostMapping("/user/registration")
    User registerUser (@RequestBody User newUser) {
        if(userAuthentificator.checkUser(newUser.getName(),  newUser.getPassword()) == 2) {
           if(userAuthentificator.addUser(newUser.getName(),newUser.getPassword()) != -1){
               return newUser;
           }
           else {
               return new User(-4, "Failed Database Create", "-");
           }
        } else {
            return new User(-3, "Username is already used", "-");
        }
    }

    @GetMapping("/users")
    public User user(@RequestParam(value="name", defaultValue = "World") String name) {
        return new User(1,name, "1234");
    }

}
