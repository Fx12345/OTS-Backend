package game_core;

import hello.StartCondition;

public class Player {
    private String id;
    private String name;
    private StartCondition startCondition;

    private boolean hasRoundFinished;

    //KPIs
    private KeyFigure oilAmount;
    private KeyFigure fuelAmount;
    private KeyFigure cash;
    private KeyFigure percentWT;
    private KeyFigure oilWellNumber;
    private KeyFigure oilTowerNumber;
    private KeyFigure rafineryNumber;
    private KeyFigure nationalResellers;
    private KeyFigure kmPipeline;

    public Player(String id, String name, StartCondition startCondition) {
        this.id = id;
        this.name = name;
        this.startCondition = startCondition;

        //initialize KPIs
        oilAmount = new KeyFigure("Oil Amount",0,KeyFigureType.Oil);
        fuelAmount = new KeyFigure("Fuel Amount", 0, KeyFigureType.Fuel);
        cash = new KeyFigure("Cash", 1000000000,KeyFigureType.Currency);
        percentWT = new KeyFigure("% of Worldtrade", 0, KeyFigureType.Percentage);
        oilWellNumber = new KeyFigure("Oil Wells", 0, KeyFigureType.Number);
        oilTowerNumber = new KeyFigure("Oil Towers", 0, KeyFigureType.Number);
        rafineryNumber = new KeyFigure("Rafinieries", 0, KeyFigureType.Number);
        nationalResellers = new KeyFigure("National Resellers", 0, KeyFigureType.Number);
        kmPipeline = new KeyFigure("km Pipelines", 0, KeyFigureType.Distance);

        executeStartCondition();

    }

    private void executeStartCondition(){
        if (this.startCondition == StartCondition.Businessman){
            cash.setValue(cash.getValue() + 1000000000 );
        } else if (this.startCondition == StartCondition.Sheikh){
            cash.setValue(cash.getValue() + 2000000000 );
        } else if (this.startCondition == StartCondition.Dictator){
            cash.setValue(cash.getValue() + 500000000 );
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StartCondition getStartCondition() {
        return startCondition;
    }

    public void setStartCondition(StartCondition startCondition) {
        this.startCondition = startCondition;
    }

    public KeyFigure getOilAmount() {
        return oilAmount;
    }

    public void setOilAmount(KeyFigure oilAmount) {
        this.oilAmount = oilAmount;
    }

    public KeyFigure getFuelAmount() {
        return fuelAmount;
    }

    public void setFuelAmount(KeyFigure fuelAmount) {
        this.fuelAmount = fuelAmount;
    }

    public KeyFigure getCash() {
        return cash;
    }

    public void setCash(KeyFigure cash) {
        this.cash = cash;
    }

    public KeyFigure getPercentWT() {
        return percentWT;
    }

    public void setPercentWT(KeyFigure percentWT) {
        this.percentWT = percentWT;
    }

    public KeyFigure getOilWellNumber() {
        return oilWellNumber;
    }

    public void setOilWellNumber(KeyFigure oilWellNumber) {
        this.oilWellNumber = oilWellNumber;
    }

    public KeyFigure getOilTowerNumber() {
        return oilTowerNumber;
    }

    public void setOilTowerNumber(KeyFigure oilTowerNumber) {
        this.oilTowerNumber = oilTowerNumber;
    }

    public KeyFigure getRafineryNumber() {
        return rafineryNumber;
    }

    public void setRafineryNumber(KeyFigure rafineryNumber) {
        this.rafineryNumber = rafineryNumber;
    }

    public KeyFigure getNationalResellers() {
        return nationalResellers;
    }

    public void setNationalResellers(KeyFigure nationalResellers) {
        this.nationalResellers = nationalResellers;
    }

    public KeyFigure getKmPipeline() {
        return kmPipeline;
    }

    public void setKmPipeline(KeyFigure kmPipeline) {
        this.kmPipeline = kmPipeline;
    }

    public boolean hasRoundFinished() {
        return hasRoundFinished;
    }

    public void setHasRoundFinished(boolean hasRoundFinished) {
        this.hasRoundFinished = hasRoundFinished;
    }

}
