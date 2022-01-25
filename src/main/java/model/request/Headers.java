package model.request;

import model.Pair;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Headers {
    private final List<Pair> list;

    public Headers(final List<Pair> list) {
        this.list = Objects.requireNonNull(list);
    }

    public List<Pair> getList() {
        return Collections.unmodifiableList(list);
    }
}
