package webserver.http.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static util.HttpRequestUtils.*;

public class CustomRequestHeader {
    private List<Pair> headerKeyValueList;
    private CustomRequestParam customRequestParam;
    private Map<String, String> requestLineMap;

    public CustomRequestHeader() {
        headerKeyValueList = new ArrayList<>();
        customRequestParam = new CustomRequestParam();
        requestLineMap = new HashMap<>();
    }

    public void addHeaderData(Pair data) {
        headerKeyValueList.add(data);
    }

    public Map<String, String> getRequestLineMap() {
        return requestLineMap;
    }

    public void setRequestLineData(Map<String, String> requestLineData) {
        for (String s : requestLineData.keySet()) {
            requestLineMap.put(s, requestLineData.get(s));
        }
    }

    public void setRequestParam(Map<String, String> param) {
        customRequestParam.addRequestParam(param);
    }

    public Map<String, String> getRequestParam() {
        return customRequestParam.getRequestParam();
    }

    public String getRequestUrl() {
        return requestLineMap.get("uri");
    }

    public String getRequestHeaderAttr(String key) {
        for (Pair pair : headerKeyValueList) {
            if(pair.getKey().equals(key)) {
                return pair.getValue().split(";")[0];
            }
        }
        return null;
    }
}
