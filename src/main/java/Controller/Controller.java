package Controller;

import java.util.function.Consumer;

public interface Controller{
    <T> Consumer<T> parseUrlInfo(String url);

    default void run(String url){

    }
}
