package hello;

public class ActionEntity {

    private String territoryId;

    private long newFuelPrice;

    private long newOilPrice;

    private int numberOfOilWells;
    private int numberOfOilTowers;
    private int numberOfRafinery;
    private int numberOfResellers;

    public ActionEntity() {}

    public String getTerritoryId() {
        return territoryId;
    }

    public void setTerritoryId(String territoryId) {
        this.territoryId = territoryId;
    }

    public long getNewFuelPrice() {
        return newFuelPrice;
    }

    public void setNewFuelPrice(long newFuelPrice) {
        this.newFuelPrice = newFuelPrice;
    }

    public long getNewOilPrice() {
        return newOilPrice;
    }

    public void setNewOilPrice(long newOilPrice) {
        this.newOilPrice = newOilPrice;
    }

    public int getNumberOfOilWells() {
        return numberOfOilWells;
    }

    public void setNumberOfOilWells(int numberOfOilWells) {
        this.numberOfOilWells = numberOfOilWells;
    }

    public int getNumberOfOilTowers() {
        return numberOfOilTowers;
    }

    public void setNumberOfOilTowers(int numberOfOilTowers) {
        this.numberOfOilTowers = numberOfOilTowers;
    }

    public int getNumberOfRafinery() {
        return numberOfRafinery;
    }

    public void setNumberOfRafinery(int numberOfRafinery) {
        this.numberOfRafinery = numberOfRafinery;
    }

    public int getNumberOfResellers() {
        return numberOfResellers;
    }

    public void setNumberOfResellers(int numberOfResellers) {
        this.numberOfResellers = numberOfResellers;
    }
}
