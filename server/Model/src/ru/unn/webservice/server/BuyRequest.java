package ru.unn.webservice.server;

public class BuyRequest implements IRequest {
    public String username;
    public String algorithmName;

    public BuyRequest(String username, String algorithmName) {
        this.username = username;
        this.algorithmName = algorithmName;
    }
}
