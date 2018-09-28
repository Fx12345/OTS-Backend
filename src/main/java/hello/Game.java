package hello;

import com.fasterxml.jackson.annotation.JsonIgnore;
import game_core.GameCoordinator;

import java.util.ArrayList;
import java.util.List;

public class Game {

    public enum State { New, Running, Finished }

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

    public void joinUser(User user, StartCondition startCondition) {
        if(isUserJoined(user)) return;

        joinedUsers.add(new JoinedUser(user, startCondition));
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

    public GameCoordinator getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(GameCoordinator coordinator) {
        this.coordinator = coordinator;
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
