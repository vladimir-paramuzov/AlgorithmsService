package ru.unn.webservice.server;

public class AddAlgorithmResponse implements IResponse {
    public String status;

    public AddAlgorithmResponse(String status) {
        this.status = status;
    }
}
