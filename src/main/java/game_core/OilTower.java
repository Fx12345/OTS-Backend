package game_core;

public class OilTower extends Structure {
    public OilTower(int id, Point coordinates, boolean visible, double oilCapacity, Territory territory, Player owner) {
        super(id, coordinates, visible, oilCapacity, territory, owner, "OilTower");
    }
}
