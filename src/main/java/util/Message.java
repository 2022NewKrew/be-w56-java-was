package util;

public class Message {
    private final String message;
    private final String returnUrl;

    public Message(String message, String returnUrl) {
        this.message = message;
        this.returnUrl = returnUrl;
    }

    public String getMessage() {
        return message;
    }

    public String getReturnUrl() {
        return returnUrl;
    }
}
