package model;

import java.util.Map;
import java.util.function.Supplier;

public interface Mapper {
    public Map<String, Supplier<String>> getMap();
}
