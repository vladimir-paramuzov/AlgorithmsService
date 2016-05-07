package ru.unn.webservice.server;

public class AddResponse implements IResponse {
    public String status;

    public AddResponse(String status) {
        this.status = status;
    }
}
