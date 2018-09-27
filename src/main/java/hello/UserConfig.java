package hello;

public class UserConfig {

    private StartCondition startCondition;

    private String gameName;

    public StartCondition getStartCondition() {
        return startCondition;
    }

    public void setStartCondition(StartCondition startCondition) {
        this.startCondition = startCondition;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
