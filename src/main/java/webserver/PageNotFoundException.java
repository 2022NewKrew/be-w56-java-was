package webserver;

public class PageNotFoundException extends Exception {

    public PageNotFoundException() {
        super("Page를 찾을 수 없습니다.");
    }
}
