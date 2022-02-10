package db;

public class Sort {

    private final Direction direction;
    private final String property;

    public Sort(Direction direction, String property) {
        this.direction = direction;
        this.property = property;
    }

    public Direction getDirection() {
        return direction;
    }

    public String getProperty() {
        return property;
    }

    public static enum Direction {
        ASC, DESC;
    }
}
