package webserver.framwork.http;

import lombok.ToString;

import java.util.*;


public class Header implements Iterable<Header.Pair> {

    private final List<Pair> headers = new ArrayList<>();
    private final Map<String, String> cookies = new HashMap<>();

    public void addValue(String key, String value) {
        if(key.equals("Cookie")){
            String[] cookies = value.split(";");
            for(String cookie : cookies){
                String[] pair = cookie.trim().split("=");
                this.cookies.put(pair[0].trim(), pair[1].trim());
            }
            return;
        }
        headers.add(new Pair(key.trim(), value.trim()));
    }

    public String getValue(String key) {
        return headers.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElse(new Pair(key, ""))
                .getValue();
    }

    public String getCookie(String key){
        return this.cookies.get(key);
    }

    @Override
    public Iterator<Pair> iterator() {
        return headers.iterator();
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(String key : cookies.keySet()){
            sb.append(key + " = " + cookies.get(key) + "\n");
        }
        return sb.toString();
    }

    @ToString
    public static class Pair {
        String key;
        String value;

        public Pair(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }

}
