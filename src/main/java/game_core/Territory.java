package game_core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Territory {

    private String id;

    private String designation;

    private int population;

    private double area;

    private double populationDensity;

    private short wealth;

    private double marketEfficiencyCoefficient;


    private List<Structure> structures;


    public static Territory readFromResultSet(ResultSet resultSet) throws SQLException {
        Territory ter = new Territory();

        ter.id = resultSet.getString("id");
        ter.designation = resultSet.getString("designation");
        ter.population = resultSet.getInt("population");
        ter.area = resultSet.getDouble("area");
        ter.populationDensity = resultSet.getDouble("population_density");
        ter.wealth = (short)resultSet.getInt("wealth");

        return ter;
    }


    public String getId() {
        return id;
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

    public double getMarketEfficiencyCoefficient() {
        return marketEfficiencyCoefficient;
    }
}
