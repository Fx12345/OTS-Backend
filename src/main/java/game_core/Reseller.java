package game_core;

public class Reseller extends Structure {
    public Reseller(int id, Point coordinates, boolean visible, double oilCapacity, Territory territory, Player owner) {
        super(id, coordinates, visible, oilCapacity, territory, owner, "Reseller");
    }
}
