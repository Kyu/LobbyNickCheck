package me.preciouso.lobbynickcheck.MojangWrapper;

public class ResponseHandler {
    private int statusCode;
    private String body;

    public ResponseHandler() {
        this(null, -1);
    }

    public ResponseHandler(String body, int statusCode) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }
}
