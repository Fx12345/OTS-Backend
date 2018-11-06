package hello;

import game_core.Player;
import game_core.Territory;

import java.util.List;

public class EntityCollection {


    private List<Player> players;
    private List<Territory> territories;

    //private List<Contract> contracts;

    public EntityCollection() {}

    public EntityCollection(List<Territory> territories, List<Player> players) {
        this.territories = territories;
        this.players = players;
    }

    public EntityCollection(List<Territory> territories) {
        this.territories = territories;
    }

    public List<Territory> getTerritories() {
        return territories;
    }

    public void setTerritories(List<Territory> territories) {
        this.territories = territories;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
