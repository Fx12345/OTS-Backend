package game_core;

public class Rafinery extends Structure {
    public Rafinery(int id, Point coordinates, boolean visible, double oilCapacity, Territory territory, Player owner) {
        super(id, coordinates, visible, oilCapacity, territory, owner, "Rafinery");
    }
}
