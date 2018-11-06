package hello;

import com.fasterxml.jackson.annotation.JsonIgnore;
import game_core.GameCoordinator;
import game_core.Player;

import java.util.ArrayList;
import java.util.List;

public class Game {

    public enum State { New, Running, Waiting, Finished }

    private String name;

    private State state;

    @JsonIgnore
    private GameCoordinator coordinator;

    private List<JoinedUser> joinedUsers;

    public Game() {
        this.state = State.New;
        this.joinedUsers = new ArrayList<>();
    }


    public boolean isUserJoined(User user) {
        for(JoinedUser ju : joinedUsers) {
            if(ju.user.getName().equals(user.getName())) {
                return true;
            }
        }

        return false;
    }

    public JoinedUser getUserJoined(User user) {
        for(JoinedUser ju : joinedUsers) {
            if(ju.user.getName().equals(user.getName())) {
                return ju;
            }
        }

        return null;
    }

    public void joinUser(User user, StartCondition startCondition) {
        if(isUserJoined(user)) return;

        joinedUsers.add(new JoinedUser(user, startCondition));
    }
    public void leaveUser(User user) {
        if(!isUserJoined(user)) return;
        joinedUsers.remove(getUserJoined(user));
    }



    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<JoinedUser> getJoinedUsers() { return this.joinedUsers; }

    public int countJoinedUsers() {return this.joinedUsers.size();}

    public GameCoordinator getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(GameCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    public List<Player> deliverPlayers(){
        List<Player> result= new ArrayList<>();
        for(JoinedUser jUser: joinedUsers){
            result.add(new Player(Integer.toString(jUser.user.getId()),jUser.user.getName(),jUser.startCondition));
        }
        return result;
    }

    private class JoinedUser {
        public final User user;
        public final StartCondition startCondition;

        public JoinedUser(User user, StartCondition startCondition) {
            this.user = user;
            this.startCondition = startCondition;
        }
    }

}
