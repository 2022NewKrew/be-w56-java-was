package util;

public enum HttpStatus {
    Ok(200, "Ok"),
    Redirect(302, "Found"),
    BadRequest(400, "Bad Request"),
    Unauthorized(401, "Unauthorized"),
    Forbidden(403, "Forbidden"),
    NotFound(404, "Not Found");

    private final int statusCode;
    private final String msg;

    HttpStatus(int statusCode, String msg){
        this.statusCode = statusCode;
        this.msg = msg;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public static String valueOf(int statusCode){
        for(HttpStatus httpStatus : HttpStatus.values()){
            if (httpStatus.statusCode == statusCode){
                return httpStatus.getMsg();
            }
        }
        return null;
    }
}
