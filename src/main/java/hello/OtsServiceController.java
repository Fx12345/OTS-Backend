package hello;

import game_core.GameCoordinator;
import org.springframework.web.bind.annotation.*;
import user_management.UserAuthentificator;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
public class OtsServiceController {

    private UserAuthentificator userAuthentificator = new UserAuthentificator();

    private Random sessionIdGenerator = new Random(System.currentTimeMillis());
    private List<Session> activeSessions = new ArrayList<>();

    private List<Game> availableGames = new ArrayList<>();


    private Session getSession(String session) {
        int sessionId = Integer.parseInt(session);

        for(Session s : activeSessions) {
            if(s.getId() == sessionId){
                return s;
            }
        }

        return null;
    }

    //Method for login Users
    @PostMapping("/user/login")
    User checkUser(@RequestBody User user, HttpServletResponse response) {
        if (userAuthentificator.checkUser(user.getName(), user.getPassword()) == 0) {
            //System.out.println("User checked");
            user.setPassword("");
            Session s = new Session(sessionIdGenerator.nextInt(), user);

            activeSessions.add(s);
            Cookie cookie = new Cookie("session", Integer.toString(s.getId()));
            cookie.setPath("/");
            response.addCookie(cookie);

            return user;
        }
        if (userAuthentificator.checkUser(user.getName(), user.getPassword()) == 1) {
            return new User(-1, "wrong password", "-");
        } else {
            return new User(-2, "no User found", "-");
        }
    }

    @GetMapping("/user/logout")
    void logoutUser(@CookieValue(name = "session", defaultValue = "-1") String session) {
        int sessionId = Integer.parseInt(session);

        for(Session s : activeSessions) {
            if(s.getId() == sessionId){
                activeSessions.remove(s);
                break;
            }
        }
    }

    //Method for register new Users
    @PostMapping("/user/registration")
    User registerUser(@RequestBody User newUser, HttpServletResponse response) {
        if (userAuthentificator.checkUser(newUser.getName(), newUser.getPassword()) == 2) {
            if (userAuthentificator.addUser(newUser.getName(), newUser.getPassword()) != -1) {
                Session s = new Session(sessionIdGenerator.nextInt(), newUser);
                newUser.setPassword("");

                activeSessions.add(s);
                Cookie cookie = new Cookie("session", Integer.toString(s.getId()));
                cookie.setPath("/");
                response.addCookie(cookie);

                return newUser;
            } else {
                return new User(-4, "Failed Database Create", "-");
            }
        } else {
            return new User(-3, "Username is already used", "-");
        }
    }


    @GetMapping("/user/config_meta")
    UserConfigMetadata getUserConfigMeta(@CookieValue(name = "session", defaultValue = "-1") String session) {
        Session s = getSession(session);

        if(s == null ||s.getCurrentGame() != null) return null;

        List<Game> joinableGames = new ArrayList<>();
        for(Game g : availableGames) {
            if(g.getState() == Game.State.New)
                joinableGames.add(g);
        }


        UserConfigMetadata ucm = new UserConfigMetadata();
        ucm.setAvailableGames(joinableGames);

        return ucm;
    }

    @PostMapping("/user/config")
    void setUserConfig(@CookieValue(name = "session", defaultValue = "-1") String session, @RequestBody UserConfig userConfig) {
        Session s = getSession(session);

        if(s == null ||s.getCurrentGame() != null) return;

        Game game = null;
        for(Game g : availableGames) {
            if(g.getName().equals(userConfig.getGameName())) {
                if(g.getState() == Game.State.New) {
                    game = g;
                } else {
                    //TODO error
                }
            }
        }

        if(game == null) {
            game = new Game();
            game.setName(userConfig.getGameName());
            availableGames.add(game);
        }

        game.joinUser(s.getUser(), userConfig.getStartCondition());
        s.setCurrentGame(game);
    }


    @GetMapping("/game/state")
    Game getGameState(@CookieValue(name = "session", defaultValue = "-1") String session) {
        Session s = getSession(session);

        if(s == null ||s.getCurrentGame() == null) return null;

        return s.getCurrentGame();

    }

    @PostMapping("/game/start")
    Game startGame(@CookieValue(name = "session", defaultValue = "-1") String session) {
        Session s = getSession(session);

        if(s == null || s.getCurrentGame() == null || s.getCurrentGame().getState() != Game.State.New) return null;

        s.getCurrentGame().setState(Game.State.Running);

        GameCoordinator gc = new GameCoordinator(s.getCurrentGame());
        s.getCurrentGame().setCoordinator(gc);

        gc.initialize();

        return s.getCurrentGame();
    }

    @GetMapping("/game/entities")
    EntityCollection getEntities(@CookieValue(name = "session", defaultValue = "-1") String session) {
        Session s = getSession(session);

        if(s == null || s.getCurrentGame() == null || s.getCurrentGame().getState() == Game.State.New) return null;


        EntityCollection entities = new EntityCollection(s.getCurrentGame().getCoordinator().getTerritoryList());


        return entities;
    }


//    @PostMapping("/game/user_actions")
//    @GetMapping("/game/finished_state")


    //Test Method for checking JSON Syntax
    @GetMapping("/users")
    public User user(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new User(1, name, "1234");
    }

}
