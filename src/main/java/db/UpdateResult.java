package db;

public class UpdateResult {

    private final int affectedRows;
    private final long key;

    public UpdateResult(int affectedRows, long key) {
        this.affectedRows = affectedRows;
        this.key = key;
    }

    public int getAffectedRows() {
        return affectedRows;
    }

    public long getKey() {
        return key;
    }
}
