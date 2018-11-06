package game_core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Territory {

    private String id;

    private String designation;

    private int population;

    private double area;

    private double populationDensity;

    private short wealth;

    private int maxOilConsumption;

    private int maxFuelConsumption;

    private double marketEfficiencyCoefficient;

    private List<Structure> structures;

    private List<Player> resellerList;

    //Final values for each territory

    private KeyFigure oilWellPrice;

    private KeyFigure oilTowerPrice;

    private KeyFigure rafineryPrice;

    private KeyFigure resellerPrice;

    //User Depenendences

    private Map<String, KeyFigure> terOilPrice = new HashMap<>();

    private Map<String, KeyFigure> terFuelPrice = new HashMap<>();

    private Map<String, KeyFigure> terOilSold = new HashMap<>();

    private Map<String, KeyFigure> terFuelSold = new HashMap<>();

    private Map<String, KeyFigure> terIncome = new HashMap<>();

    private Map<String, KeyFigure> terOilWin = new HashMap<>();

    private Map<String, KeyFigure> terFuelProcessed = new HashMap<>();

    private List<Structure> oilWells = new ArrayList<>();

    private List<Structure> oilTowers = new ArrayList<>();

    private List<Structure> rafineries = new ArrayList<>();

    private List<Structure> resellers = new ArrayList<>();

    private Map<String, KeyFigure> playerOW = new HashMap<>();

    private Map<String, KeyFigure> playerOT = new HashMap<>();

    private Map<String, KeyFigure> playerR = new HashMap<>();

    private Map<String, KeyFigure> playerRe = new HashMap<>();

    //Constants

    private static final long RESELLER_PRICE = 10000000;

    private static final long RAFINERY_PRICE = 100000000;

    private static final long OILTOWER_PRICE = 50000000;

    private static final long OILWELL_PRICE = 200000000;

    public Territory() {

    }


    public static Territory readFromResultSet(ResultSet resultSet, List<Player> players) throws SQLException {
        Territory ter = new Territory();

        ter.id = resultSet.getString("id");
        ter.designation = resultSet.getString("designation");
        ter.population = resultSet.getInt("population");
        ter.area = resultSet.getDouble("area");
        ter.populationDensity = resultSet.getDouble("population_density");
        ter.wealth = (short)resultSet.getInt("wealth");
        ter.maxOilConsumption = resultSet.getInt("max_oil_consumption");
        ter.maxFuelConsumption = (int) (resultSet.getInt("max_oil_consumption") * 0.7);

        ter.resellerList = new ArrayList<>();
        ter.structures = new ArrayList<>();

        ter.calculateMarketEfficiencyCoefficient();
        ter.calculateStructureCosts();
        ter.setupUserDependencies(players);

        for(Player player : players) {
            ter.terOilPrice.put(player.getName(), new KeyFigure("OilPrice", 0, KeyFigureType.Currency));
            ter.terFuelPrice.put(player.getName(), new KeyFigure("FuelPrice", 0, KeyFigureType.Currency));
        }

        return ter;
    }

    private void setupUserDependencies(List<Player> players){
        players.forEach(player -> {
            terOilPrice.put(player.getName(), new KeyFigure("terOilPrice", 0.0, KeyFigureType.Currency));
            terFuelPrice.put(player.getName(), new KeyFigure("terFuelPrice", 0.0, KeyFigureType.Currency));
            terOilSold.put(player.getName(), new KeyFigure("terOilSold", 0.0, KeyFigureType.Oil));
            terFuelSold.put(player.getName(), new KeyFigure("terFuelSold", 0.0, KeyFigureType.Fuel));
            terIncome.put(player.getName(), new KeyFigure("terIncome", 0.0, KeyFigureType.Currency));
            terOilWin.put(player.getName(), new KeyFigure("terOilWin", 0.0, KeyFigureType.Oil));
            terFuelProcessed.put(player.getName(), new KeyFigure("terFuelProcessed", 0.0, KeyFigureType.Fuel));

            playerOW.put(player.getName(), new KeyFigure("playerOW", 0, KeyFigureType.Number));
            playerOT.put(player.getName(), new KeyFigure("playerOT", 0, KeyFigureType.Number));
            playerR.put(player.getName(), new KeyFigure("playerR", 0, KeyFigureType.Number));
            playerRe.put(player.getName(), new KeyFigure("playerRe", 0, KeyFigureType.Number));
        });
    }

    private void calculateStructureCosts(){
        oilWellPrice = new KeyFigure("oilWellPrice", calculatePriceWithMarketVariance(OILWELL_PRICE), KeyFigureType.Currency);
        oilTowerPrice = new KeyFigure("oilTowerPrice",calculatePriceWithMarketVariance(OILTOWER_PRICE), KeyFigureType.Currency);
        rafineryPrice = new KeyFigure("rafineryPrice",calculatePriceWithMarketVariance(RAFINERY_PRICE), KeyFigureType.Currency);
        resellerPrice = new KeyFigure("resellerPrice",calculatePriceWithMarketVariance(RESELLER_PRICE), KeyFigureType.Currency);
    }

    private long calculatePriceWithMarketVariance(long price) {
        return (long)(price * (1 + 0.3 * (this.marketEfficiencyCoefficient - 0.5)));
    }

    private void calculateMarketEfficiencyCoefficient(){
        this.marketEfficiencyCoefficient = (this.populationDensity / this.wealth) / 350;
    }


    public String getId() {
        return id;
    }

    public void setOilPrice(long price, Player player) {
        this.terOilPrice.get(player.getName()).setValue(price);
    }

    public void setFuelPrice(long price, Player player) {
        this.terFuelPrice.get(player.getName()).setValue(price);
    }

    public String getDesignation() {
        return designation;
    }

    public int getPopulation() {
        return population;
    }

    public double getArea() {
        return area;
    }

    public double getPopulationDensity() {
        return populationDensity;
    }

    public short getWealth() {
        return wealth;
    }

    public int getMaxOilConsumption() {
        return maxOilConsumption;
    }

    public int getMaxFuelConsumption() {
        return maxFuelConsumption;
    }

    public double getMarketEfficiencyCoefficient() {
        return marketEfficiencyCoefficient;
    }

    public List<Player> getResellerList() {
        return resellerList;
    }

    public void setResellerList(List<Player> hasResellerList) {
        this.resellerList = hasResellerList;
    }

    public boolean isOnResllerList(Player player){
        return resellerList.contains(player);
    }

    public KeyFigure getOilWellPrice() {
        return oilWellPrice;
    }

    public KeyFigure getOilTowerPrice() {
        return oilTowerPrice;
    }

    public KeyFigure getRafineryPrice() {
        return rafineryPrice;
    }

    public KeyFigure getResellerPrice() {
        return resellerPrice;
    }

    public Map<String, KeyFigure> getTerOilPrice() {
        return terOilPrice;
    }

    public Map<String, KeyFigure> getTerFuelPrice() {
        return terFuelPrice;
    }

    public Map<String, KeyFigure> getTerOilSold() {
        return terOilSold;
    }

    public Map<String, KeyFigure> getTerFuelSold() {
        return terFuelSold;
    }

    public Map<String, KeyFigure> getTerIncome() {
        return terIncome;
    }

    public Map<String, KeyFigure> getTerOilWin() {
        return terOilWin;
    }

    public Map<String, KeyFigure> getTerFuelProcessed() {
        return terFuelProcessed;
    }
    public List<Structure> getStructures() {
        return structures;
    }

    public void setStructures(List<Structure> structures) {
        this.structures = structures;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Structure> getOilWells() {
        return oilWells;
    }

    public void setOilWells(List<Structure> oilWells) {
        this.oilWells = oilWells;
    }

    public List<Structure> getOilTowers() {
        return oilTowers;
    }

    public void setOilTowers(List<Structure> oilTowers) {
        this.oilTowers = oilTowers;
    }

    public List<Structure> getRafinerys() {
        return rafineries;
    }

    public void setRafinerys(List<Structure> rafineries) {
        this.rafineries = rafineries;
    }

    public List<Structure> getResellers() {
        return resellers;
    }

    public void setResellers(List<Structure> resellers) {
        this.resellers = resellers;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Map<String, KeyFigure> getPlayerOW() {
        return playerOW;
    }

    public void setPlayerOW(Map<String, KeyFigure> playerOW) {
        this.playerOW = playerOW;
    }

    public Map<String, KeyFigure> getPlayerOT() {
        return playerOT;
    }

    public void setPlayerOT(Map<String, KeyFigure> playerOT) {
        this.playerOT = playerOT;
    }

    public Map<String, KeyFigure> getPlayerR() {
        return playerR;
    }

    public void setPlayerR(Map<String, KeyFigure> playerR) {
        this.playerR = playerR;
    }

    public Map<String, KeyFigure> getPlayerRe() {
        return playerRe;
    }

    public void setPlayerRe(Map<String, KeyFigure> playerRe) {
        this.playerRe = playerRe;
    }

    public void addStructure(Structure structure){
        switch (structure.getType()){
            case "OilWell":
                this.oilWells.add(structure);
                this.playerOW.get(structure.getOwnerId()).setValue(oilWells.stream().filter(o-> o.getOwnerId().equals(structure.getOwnerId())).collect(Collectors.toList()).size());
                break;
            case "OilTower":
                this.oilTowers.add(structure);
                this.playerOT.get(structure.getOwnerId()).setValue(oilTowers.stream().filter(o-> o.getOwnerId().equals(structure.getOwnerId())).collect(Collectors.toList()).size());
                break;
            case "Rafinery":
                this.rafineries.add(structure);
                this.playerR.get(structure.getOwnerId()).setValue(rafineries.stream().filter(o-> o.getOwnerId().equals(structure.getOwnerId())).collect(Collectors.toList()).size());
                break;
            case"Reseller":
                this.resellers.add(structure);
                this.playerRe.get(structure.getOwnerId()).setValue(resellers.stream().filter(o-> o.getOwnerId().equals(structure.getOwnerId())).collect(Collectors.toList()).size());
                break;
        }
        this.structures.add(structure);


    }
    public void addReseller(Player player){
        this.resellerList.add(player);
    }
}
