package ru.unn.webservice.server;

public class BuyResponse implements IResponse {
    public String status;

    public BuyResponse(String status) {
        this.status = status;
    }
}
