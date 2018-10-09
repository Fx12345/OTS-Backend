package game_core;

import hello.Config;
import hello.Game;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameCoordinator {

    private final Game initialGame;

    private Map<String, Territory> territoryMap = new HashMap<>();

    public GameCoordinator(Game initialGame) {
        this.initialGame = initialGame;
    }

    public void initialize() {

        try {
            Connection conn = connectToDB();

            PreparedStatement stmt = conn.prepareStatement("SELECT id, designation, population, area, population_density, wealth FROM territory");
            ResultSet result = stmt.executeQuery();

            while(result.next()) {
                Territory ter = Territory.readFromResultSet(result);
                territoryMap.put(ter.getId(), ter);
            }

        } catch (SQLException ex) {
            System.out.println("SQL Exception while reading territories drom db");
        }

        //TODO initialize empty structures

        //TODO assign user structures
    }



    private Connection connectToDB() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(Config.DB_HOST, Config.DB_USER, Config.DB_PASSWORD);
            //System.out.println("Connected to PostgreSQL server");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public List<Territory> getTerritoryList() {
        return new ArrayList<>(territoryMap.values());
    }
}
