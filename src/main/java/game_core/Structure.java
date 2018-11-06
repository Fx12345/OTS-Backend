package game_core;

import com.fasterxml.jackson.annotation.JsonGetter;
import hello.User;

public abstract class Structure {

    protected int id;

    private Point coordinates;

    private boolean visible;

    private double oilCapacity;

    private String type;

    private Territory territory;

    private Player owner;


    public Structure() {}

    public Structure(int id, Point coordinates, boolean visible, double oilCapacity, Territory territory,Player owner, String type) {
        this.id = id;
        this.coordinates = coordinates;
        this.visible = visible;
        this.oilCapacity = oilCapacity;
        this.territory = territory;
        this.owner = owner;
        this.type = type;
    }

    @JsonGetter("territory")
    public String getTerritoryId() {
        return territory.getId();
    }

    @JsonGetter("owner")
    public String getOwnerId() {
        return owner.getName();
    }


    public int getId() {
        return id;
    }

    public Point getCoordinates() {
        return coordinates;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public double getOilCapacity() {
        return oilCapacity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
