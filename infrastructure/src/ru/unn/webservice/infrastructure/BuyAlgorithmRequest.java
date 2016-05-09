package ru.unn.webservice.infrastructure;

public class BuyAlgorithmRequest implements IRequest {
    public String userName;
    public String algorithmName;

    public BuyAlgorithmRequest(String userName, String algorithmName) {
        this.userName = userName;
        this.algorithmName = algorithmName;
    }
}
