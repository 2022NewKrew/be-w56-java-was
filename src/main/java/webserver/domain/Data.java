package webserver.domain;

import java.util.HashMap;
import java.util.Map;
import util.HttpRequestUtils;

public class Data {

    private final Map<String, String> data;

    private Data(Map<String, String> data) {
        this.data = new HashMap<>(data);
    }

    public Data(Data data) {
        this(data.data);
    }

    public static Data createData(String data) {
        return new Data(HttpRequestUtils.parseData(data));
    }

    public String getDataAttribute(String key) {
        return data.get(key);
    }
}
