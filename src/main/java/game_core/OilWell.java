package game_core;

public class OilWell extends Structure {
    public OilWell(int id, Point coordinates, boolean visible, double oilCapacity, Territory territory, Player owner) {
        super(id, coordinates, visible, oilCapacity, territory, owner, "OilWell");
    }
}
