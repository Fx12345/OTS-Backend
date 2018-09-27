package hello;

import game_core.Territory;

import java.util.List;

public class EntityCollection {

    private List<Territory> territories;

    //private List<Contract> contracts;

    public EntityCollection(List<Territory> territories) {
        this.territories = territories;
    }

    public List<Territory> getTerritories() {
        return territories;
    }

    public void setTerritories(List<Territory> territories) {
        this.territories = territories;
    }
}
