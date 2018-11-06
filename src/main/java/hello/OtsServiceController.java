package hello;

import game_core.GameCoordinator;
import game_core.Player;
import game_core.Territory;
import org.springframework.web.bind.annotation.*;
import user_management.UserAuthentificator;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//Todo fetch Cross Origin to server address
//@CrossOrigin(origins = "http://localhost:8082")
@CrossOrigin
@RestController
public class OtsServiceController {

    private UserAuthentificator userAuthentificator = new UserAuthentificator();

    private Random sessionIdGenerator = new Random(System.currentTimeMillis());
    private List<Session> activeSessions = new ArrayList<>();

    private List<Game> availableGames = new ArrayList<>();

    //Get single Session
    private Session getSession(String session) {
        int sessionId = Integer.parseInt(session);

        for(Session s : activeSessions) {
            if(s.getId() == sessionId){
                return s;
            }
        }

        return null;
    }

    //----------------------------------------------------------------------------------------User Management---------------------------------------------------------------
    //Method for login Users
    @PostMapping("/user/login")
    User checkUser(@RequestBody User user, HttpServletResponse response) {
        if (userAuthentificator.checkUser(user.getName(), user.getPassword()) == 0) {
            Session s = new Session(sessionIdGenerator.nextInt(), user);
            activeSessions.add(s);
        /**    Cookie cookie = new Cookie("session", Integer.toString(s.getId()));
          //  cookie.setDomain("localhost");
            cookie.setPath("/");
            response.addCookie(cookie); **/
            user.setPassword(Integer.toString(s.getId()));
            return user;
        }
        if (userAuthentificator.checkUser(user.getName(), user.getPassword()) == 1) {
            return new User(-1, "wrong password", "-");
        } else {
            return new User(-2, "no User found", "-");
        }
    }

    @GetMapping("/user/logout")
    Session logoutUser(@RequestHeader(value = "session", defaultValue = "-1") String session) {
        int sessionId = Integer.parseInt(session);

        for(Session s : activeSessions) {
            if(s.getId() == sessionId){
                activeSessions.remove(s);
                return s;
                //break;
            }
        }
        return null;
    }

    //Method for register new Users
    @PostMapping("/user/registration")
    User registerUser(@RequestBody User newUser, HttpServletResponse response) {
        if (userAuthentificator.checkUser(newUser.getName(), newUser.getPassword()) == 2) {
            if (userAuthentificator.addUser(newUser.getName(), newUser.getPassword()) != -1) {
                Session s = new Session(sessionIdGenerator.nextInt(), newUser);
                newUser.setPassword(Integer.toString(s.getId()));
                activeSessions.add(s);
                /**
                Cookie cookie = new Cookie("session", Integer.toString(s.getId()));
                cookie.setDomain(null);
                cookie.setPath("/");
                response.addCookie(cookie);
**/
                return newUser;
            } else {
                return new User(-4, "Failed Database Create", "-");
            }
        } else {
            return new User(-3, "Username is already used", "-");
        }
    }

    @GetMapping("/user/name")
    String getUsername(@RequestHeader(value = "session", defaultValue = "-1") String session) {
        Session s = getSession(session);
        return s.getUser().getName();
    }

//----------------------------------------------------------------------------------------User Game Config---------------------------------------------------------------
    //getGames
    @GetMapping("/user/config_meta")
    UserConfigMetadata getUserConfigMeta(@RequestHeader(value = "session", defaultValue = "-1") String session) {
        Session s = getSession(session);

        if(s == null ||s.getCurrentGame() != null) return new UserConfigMetadata();

        List<Game> joinableGames = new ArrayList<>();
        for(Game g : availableGames) {
            if(g.getState() == Game.State.New && g.countJoinedUsers() < 4 && g.countJoinedUsers() > 0)
                joinableGames.add(g);
        }


        UserConfigMetadata ucm = new UserConfigMetadata();
        ucm.setAvailableGames(joinableGames);

        return ucm;
    }

    //add or join Game
    @PostMapping("/user/config")
    String setUserConfig(@RequestHeader(value = "session", defaultValue = "-1") String session, @RequestBody UserConfig userConfig) {
        Session s = getSession(session);
        if(s == null ||s.getCurrentGame() != null) return "ERROR - No Session or already in game";

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
        return "game was set - " + game.countJoinedUsers();
    }
    //leave Game
    @GetMapping("/user/leaveGame")
    String leaveGame(@RequestHeader(value = "session", defaultValue = "-1") String session) {
        Session s = getSession(session);
        Game game = s.getCurrentGame();
        game.leaveUser(s.getUser());
        s.setCurrentGame(null);
        return "user left game";
    }

    //----------------------------------------------------------------------------------------Game Setup---------------------------------------------------------------
    @GetMapping("/game/state")
    Game getGameState(@RequestHeader(value = "session", defaultValue = "-1") String session) {
        Session s = getSession(session);

        if(s == null ||s.getCurrentGame() == null) return null;

        return s.getCurrentGame();

    }

    @PostMapping("/game/start")
    Game startGame(@RequestHeader(value = "session", defaultValue = "-1") String session) {
        Session s = getSession(session);

        if(s == null || s.getCurrentGame() == null || s.getCurrentGame().getState() != Game.State.New) return null;

        s.getCurrentGame().setState(Game.State.Running);

        GameCoordinator gc = new GameCoordinator(s.getCurrentGame());
        s.getCurrentGame().setCoordinator(gc);

        gc.initialize();

        return s.getCurrentGame();
    }

    @GetMapping("/game/entities")
    EntityCollection getEntities(@RequestHeader(value = "session", defaultValue = "-1") String session) {
        Session s = getSession(session);

        if(s == null || s.getCurrentGame() == null || s.getCurrentGame().getState() == Game.State.New) return null;


        EntityCollection entities = new EntityCollection(s.getCurrentGame().getCoordinator().getTerritoryList(), s.getCurrentGame().getCoordinator().getPlayers());


        return entities;
    }

//----------------------------------------------------------------------------------------------------Run Game--------------------------------------------------------

    @PostMapping("/game/action")
    String userAction(@RequestHeader(value = "session", defaultValue = "-1") String session, @RequestBody ActionEntity actionEntity) {
        Session s = getSession(session);
        GameCoordinator gc = s.getCurrentGame().getCoordinator();
        Player pl = s.getCurrentGame().getCoordinator().getPlayers().stream().filter(p -> p.getId().equals(Integer.toString(s.getUser().getId()))).findFirst().orElse(null);
        gc.updateMap(actionEntity, pl);
        return "executed";
    }

    @PostMapping("/game/round_finished")
    String roundFinished(@RequestHeader(value = "session", defaultValue = "-1") String session) {
        Session s = getSession(session);

        GameCoordinator gc = s.getCurrentGame().getCoordinator();
        Player pl = s.getCurrentGame().getCoordinator().getPlayers().stream().filter(p -> p.getName().equals(s.getUser().getName())).findFirst().orElse(null);
        gc.setPlayerRoundFinished(pl);
        return "executed";
    }



//    @GetMapping("/game/finished_state")




    //------------------------------------------------------------------------------------Test Method for checking JSON Syntax--------------------------------------

    @GetMapping("/users")
    public User user(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new User(1, name, "1234");
    }

}
