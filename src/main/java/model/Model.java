package model;

public interface Model {
    default String getParam(String param){
        return null;
    }
}
