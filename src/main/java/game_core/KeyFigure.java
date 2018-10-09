package game_core;

public class KeyFigure {
    private String id;
    private double value;
    private KeyFigureType type;

    public KeyFigure(String id, double value, KeyFigureType type) {
        this.id = id;
        this.value = value;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public KeyFigureType getType() {
        return type;
    }

    public void setType(KeyFigureType type) {
        this.type = type;
    }


}
