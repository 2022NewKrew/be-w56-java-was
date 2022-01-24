package model.request;

import util.HttpRequestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Headers {
    private final List<HttpRequestUtils.Pair> list;

    public Headers(final List<HttpRequestUtils.Pair> list) {
        this.list = Objects.requireNonNull(list);
    }

    public List<HttpRequestUtils.Pair> getList() {
        return Collections.unmodifiableList(list);
    }
}
