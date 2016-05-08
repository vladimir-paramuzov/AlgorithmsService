package ru.unn.webservice.infrastructure;

public class BuyAlgorithmResponse implements IResponse {
    public String status;

    public BuyAlgorithmResponse(String status) {
        this.status = status;
    }
}
