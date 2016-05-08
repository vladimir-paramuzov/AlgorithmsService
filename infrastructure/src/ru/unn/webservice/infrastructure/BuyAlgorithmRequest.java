package ru.unn.webservice.infrastructure;

public class BuyAlgorithmRequest implements IRequest {
    public String username;
    public String algorithmName;

    public BuyAlgorithmRequest(String username, String algorithmName) {
        this.username = username;
        this.algorithmName = algorithmName;
    }
}
