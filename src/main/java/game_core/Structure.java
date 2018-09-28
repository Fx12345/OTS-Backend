package game_core;

import com.fasterxml.jackson.annotation.JsonGetter;
import hello.User;

public abstract class Structure {

    private int id;

    private Point coordinates;

    private boolean visible;

    private double oilCapacity;


    private Territory territory;

    private User owner;


    public Structure() {

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
}
