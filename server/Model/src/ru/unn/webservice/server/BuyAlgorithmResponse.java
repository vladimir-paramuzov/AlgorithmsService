package ru.unn.webservice.server;

public class BuyAlgorithmResponse implements IResponse {
    public String status;

    public BuyAlgorithmResponse(String status) {
        this.status = status;
    }
}
