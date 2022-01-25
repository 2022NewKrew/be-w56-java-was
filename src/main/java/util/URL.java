package util;

import java.util.Map;

public class URL {
    private final String url;
    public final Map<String, String> params;

    public URL(String url){
        String[] words = url.split("\\?");
        this.url = words[0].trim();
        Map<String, String> tempParams = null;
        if(words.length == 2)
            tempParams = HttpRequestUtils.parseQueryString(words[1]);
        params = tempParams;
    }

    public String url(){
        return this.url;
    }

    public Map<String, String> params(){
        return this.params;
    }

}
