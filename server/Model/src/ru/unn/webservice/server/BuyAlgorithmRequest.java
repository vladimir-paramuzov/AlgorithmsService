package ru.unn.webservice.server;

public class BuyAlgorithmRequest implements IRequest {
    public String username;
    public String algorithmName;

    public BuyAlgorithmRequest(String username, String algorithmName) {
        this.username = username;
        this.algorithmName = algorithmName;
    }
}
