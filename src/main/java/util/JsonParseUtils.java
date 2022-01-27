package util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonParseUtils {
    public static JSONObject stringToJson(String str) throws ParseException {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(str);
    }
}
