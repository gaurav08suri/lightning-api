package org.server.models;

public class Response {

    private Response() {

    }

    private Response(String message) {
        this.message = message;
    }

    public static Response of(String message) {
        return new Response(message);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    private String message;
}
