package ru.unn.webservice.storage;

public class GetUserDataRequest implements IDataRequest {
    public String username;

    public GetUserDataRequest(String username) {
        this.username = username;
    }
}
