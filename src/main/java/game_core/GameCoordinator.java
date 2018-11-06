package game_core;

import hello.ActionEntity;
import hello.Config;
import hello.Game;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.System.out;

public class GameCoordinator {

    public static final int DAYS_PER_PERIOD = 7;

    private static int structure_id = 0;

    private final Game initialGame;

    private Map<String, Territory> territoryMap = new HashMap<>();

    private List<Player> players;

    public GameCoordinator(Game initialGame) {
        this.initialGame = initialGame;
    }

    /**
     * Initializes the game
     */
    public void initialize() {

        players = initialGame.deliverPlayers();

        try {
            Connection conn = connectToDB();

            PreparedStatement stmt = conn.prepareStatement("SELECT id, designation, population, area, population_density, wealth, max_oil_consumption FROM territory");
            ResultSet result = stmt.executeQuery();

            while(result.next()) {
                Territory ter = Territory.readFromResultSet(result, players);
                territoryMap.put(ter.getId(), ter);
            }

        } catch (SQLException ex) {
            out.println("SQL Exception while reading territories from db");
        }


        //TODO initialize empty structures

        //TODO assign user structures
    }

    /**
     * Applies an action by a player to the map
     * @param actionEntity
     * @param player
     */
    public void updateMap(ActionEntity actionEntity, Player player){
        for(Map.Entry<String, Territory> entry: territoryMap.entrySet()){
            String key = entry.getKey();
            Territory tempTer = entry.getValue();
            List oilWells = tempTer.getStructures().stream().filter(structure -> structure instanceof  OilWell && structure.getOwnerId() == player.getName()).collect(Collectors.toList());
            List oilTowers = tempTer.getStructures().stream().filter(structure -> structure instanceof OilTower && structure.getOwnerId() == player.getName()).collect(Collectors.toList());
            List rafinieries = tempTer.getStructures().stream().filter(structure -> structure instanceof Rafinery && structure.getOwnerId() == player.getName()).collect(Collectors.toList());
            List resellers = tempTer.getStructures().stream().filter(structure -> structure instanceof Reseller && structure.getOwnerId() == player.getName()).collect(Collectors.toList());
            int oilWellCount = oilWells.size();
            int oilTowerCount = oilTowers.size();
            int rafineryCount = rafinieries.size();
            int resellerCount = resellers.size();
                    //out.println(oilTowerCount);

            if(tempTer.getId().equals(actionEntity.getTerritoryId())){
                tempTer.setOilPrice(actionEntity.getNewOilPrice(), player);
                tempTer.setFuelPrice(actionEntity.getNewFuelPrice(), player);

                for (int i = 0; i < (actionEntity.getNumberOfOilWells() - oilWellCount); i++){
                    Structure ow = new OilWell(structure_id, new Point(0,0), true, 0.0, tempTer, player);
                    tempTer.addStructure(ow);
                    player.getOilWellNumber().incrementValue();
                    structure_id++;
                }

                for(int i = 0;i < (actionEntity.getNumberOfOilTowers() - oilTowerCount); i++){
                    Structure ot = new OilTower(structure_id, new Point(0, 0), true, 0.0, tempTer, player);
                    tempTer.addStructure(ot);
                    player.getOilTowerNumber().incrementValue();
                    structure_id++;
                }

                for(int i = 0; i < (actionEntity.getNumberOfRafinery() - rafineryCount); i++){
                    Structure r = new Rafinery(structure_id, new Point(0,0), true,0.0,tempTer,player);
                    tempTer.addStructure(r);
                    player.getRafineryNumber().incrementValue();
                    structure_id++;
                }

                for(int i = 0; i < (actionEntity.getNumberOfResellers() - resellerCount); i++){
                    Structure re = new Reseller(structure_id, new Point(0,0), true, 0.0, tempTer,player);
                    tempTer.addStructure(re);
                    tempTer.addReseller(player);
                    player.getNationalResellers().incrementValue();
                    structure_id++;
                }
            }
        }
    }

    public void setPlayerRoundFinished(Player player) {
        player.setHasRoundFinished(true);
        initialGame.setState(Game.State.Waiting);

        boolean allPlayersFinished = this.players.stream().allMatch(player1 -> player1.hasRoundFinished() == true);

        if(allPlayersFinished) {
            for(Player pl : players) pl.setHasRoundFinished(false);
            // Calculate money stuff

            long oilPriceSum = 0, fuelPriceSum = 0;
            long oilPriceCount = 0, fuelPriceCount = 0;

            for(Territory t : territoryMap.values()) {
                for(KeyFigure k : t.getTerOilPrice().values()) {
                    if(k.getValue() > 0) {
                        oilPriceSum += k.getValue();
                        oilPriceCount ++;
                    }
                }

                for(KeyFigure k : t.getTerFuelPrice().values()) {
                    if(k.getValue() > 0) {
                        fuelPriceSum += k.getValue();
                        fuelPriceCount ++;
                    }
                }
            }

            long oilPriceAverage = oilPriceSum / (oilPriceCount +1);
            long fuelPriceAverage = fuelPriceSum / (fuelPriceCount+1);

            // TODO sort by average price
            for(Territory t : this.territoryMap.values()) {
                executeStructureBuildsForTerritory(t);
                calculateProductionForTerritory(t, player);
                calculateSalesForTerritory(t, oilPriceAverage, fuelPriceAverage);
            }

            initialGame.setState(Game.State.Running);
        }

    }


    private void calculateSalesForTerritory(Territory ter, long oilPriceAverage, long fuelPriceAverage) {
        // a competitor is someone, who participates in a given local market
        List<Player> oilCompetitors = new ArrayList<>();
        List<Player> fuelCompetitors = new ArrayList<>();


        // calculate local price average between competitors

        long localOilAveragePrice = 0;
        long localFuelAveragePrice = 0;

        for(Map.Entry<String, KeyFigure> k : ter.getTerOilPrice().entrySet()){
            if(k.getValue().getValue() > 0) {
                Player pl = this.players.stream().filter(p -> p.getName().equals(k.getKey())).findFirst().get();
                oilCompetitors.add(pl);

                localOilAveragePrice += k.getValue().getValue();
            }
        }

        for(Map.Entry<String, KeyFigure> k : ter.getTerFuelPrice().entrySet()){
            if(k.getValue().getValue() > 0) {
                Player pl = this.players.stream().filter(p -> p.getName().equals(k.getKey())).findFirst().get();
                fuelCompetitors.add(pl);

                localFuelAveragePrice += k.getValue().getValue();
            }
        }

        localOilAveragePrice /= (oilCompetitors.size() +1);
        localFuelAveragePrice /= (fuelCompetitors.size()+1);


        // the sale coefficient describes the influence of a competitor in a local market

        Map<Player, Double> oilSaleCoefficients = new HashMap<>();
        Map<Player, Double> fuelSaleCoefficients = new HashMap<>();


        // calculate sale coefficient per player

        for(Player player : oilCompetitors) {
            double playerOilSaleCoefficient =
                    (localOilAveragePrice + oilPriceAverage) /
                    (2 * ter.getTerOilPrice().get(player.getName()).getValue());

            oilSaleCoefficients.put(player, playerOilSaleCoefficient);
        }

        for(Player player : fuelCompetitors) {
            double playerFuelSaleCoefficient =
                    (localFuelAveragePrice + fuelPriceAverage) /
                    (2 * ter.getTerFuelPrice().get(player.getName()).getValue());

            fuelSaleCoefficients.put(player, playerFuelSaleCoefficient);
        }


        // the max sale volumes describe the maximal amount of resources,
        // that can be sold by one player in a local market

        double oilSaleCoefficientSum = oilSaleCoefficients.values().stream().mapToDouble(d -> d).sum();
        double fuelSaleCoefficientSum = oilSaleCoefficients.values().stream().mapToDouble(d -> d).sum();

        // maxOilConsumption a. maxFuelConsumption are per day, so we have to multiply with the number of days per period/round
        for(Player pl : oilCompetitors) {
            long maxOilSaleVolume = (long)(oilSaleCoefficients.get(pl) / oilSaleCoefficientSum) * ter.getMaxOilConsumption() * DAYS_PER_PERIOD;

            KeyFigure playerCash = pl.getCash();
            KeyFigure playerOil = pl.getOilAmount();
            KeyFigure playerOilSold = ter.getTerOilSold().get(pl.getName());
            KeyFigure playerIncome = ter.getTerIncome().get(pl.getName());

            long oilPrice = (long)ter.getTerOilPrice().get(pl.getName()).getValue();
            long soldOilVolume = Math.min(maxOilSaleVolume, (long)playerOil.getValue());

            playerCash.setValue(playerCash.getValue() + oilPrice * soldOilVolume);
            playerOil.setValue(playerOil.getValue() - soldOilVolume);
            playerOilSold.setValue(playerOilSold.getValue() + soldOilVolume);
            playerIncome.setValue(playerIncome.getValue() +  oilPrice * soldOilVolume);
        }

        for(Player pl : fuelCompetitors) {
            long maxFuelSaleVolume = (long)(fuelSaleCoefficients.get(pl) / fuelSaleCoefficientSum) * ter.getMaxFuelConsumption() * DAYS_PER_PERIOD;

            KeyFigure playerCash = pl.getCash();
            KeyFigure playerFuel = pl.getFuelAmount();
            KeyFigure playerFuelSold = ter.getTerOilSold().get(pl.getName());
            KeyFigure playerIncome = ter.getTerIncome().get(pl.getName());

            long fuelPrice = (long)ter.getTerFuelPrice().get(pl.getName()).getValue();
            long soldFuelVolume = Math.min(maxFuelSaleVolume, (long)playerFuel.getValue());

            playerCash.setValue(playerCash.getValue() + fuelPrice * soldFuelVolume);
            playerFuel.setValue(playerFuel.getValue() - soldFuelVolume);
            playerFuelSold.setValue(playerFuelSold.getValue() + soldFuelVolume);
            playerIncome.setValue(playerIncome.getValue() +  fuelPrice * soldFuelVolume);
        }

    }

    private void executeStructureBuildsForTerritory(Territory t) {
        //solved in updateMap()
    }

    private void calculateProductionForTerritory(Territory t, Player player){
        List oilWells = t.getStructures().stream().filter(structure -> structure instanceof  OilWell && structure.getOwnerId() == player.getName()).collect(Collectors.toList());
        List oilTowers = t.getStructures().stream().filter(structure -> structure instanceof OilTower && structure.getOwnerId() == player.getName()).collect(Collectors.toList());
        List rafinieries = t.getStructures().stream().filter(structure -> structure instanceof Rafinery && structure.getOwnerId() == player.getName()).collect(Collectors.toList());
        int oilWellCount = oilWells.size();
        int oilTowerCount = oilTowers.size();
        int rafineryCount = rafinieries.size();

        /* The following logic is simplified and has to be changed in release 2 */
        KeyFigure oilA = player.getOilAmount();
        if(oilWellCount == oilTowerCount){
           oilA.setValue(oilTowerCount * 1000000);
        } else if(oilWellCount > oilTowerCount){
            oilA.setValue(oilTowerCount * 1000000);
        }else{
            oilA.setValue(oilWellCount * 1000000);
        }
    }

    private Connection connectToDB() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(Config.DB_HOST, Config.DB_USER, Config.DB_PASSWORD);
            //System.out.println("Connected to PostgreSQL server");
        } catch (SQLException e) {
            out.println(e.getMessage());
        }
        return conn;
    }

    public List<Territory> getTerritoryList() {
        return new ArrayList<>(territoryMap.values());
    }

    public List<Player> getPlayers() {
        return players;
    }
}
