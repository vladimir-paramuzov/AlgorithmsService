package ru.unn.webservice.server;

public class ChangeBalanceRequest implements IRequest {
    public int delta;
    public String username;

    public ChangeBalanceRequest(int delta, String username) {
        this.delta = delta;
        this.username = username;
    }
}
