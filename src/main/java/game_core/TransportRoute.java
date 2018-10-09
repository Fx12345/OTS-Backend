package game_core;

public class TransportRoute {
    private String id;
    private double routeLength;
    private boolean hasPipe;
    private Structure start;
    private Structure end;

    public TransportRoute(String id, double routeLength, boolean hasPipe, Structure start, Structure end) {
        this.id = id;
        this.routeLength = routeLength;
        this.hasPipe = hasPipe;
        this.start = start;
        this.end = end;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getRouteLength() {
        return routeLength;
    }

    public void setRouteLength(double routeLength) {
        this.routeLength = routeLength;
    }

    public boolean isHasPipe() {
        return hasPipe;
    }

    public void setHasPipe(boolean hasPipe) {
        this.hasPipe = hasPipe;
    }

    public Structure getStart() {
        return start;
    }

    public void setStart(Structure start) {
        this.start = start;
    }

    public Structure getEnd() {
        return end;
    }

    public void setEnd(Structure end) {
        this.end = end;
    }
}
