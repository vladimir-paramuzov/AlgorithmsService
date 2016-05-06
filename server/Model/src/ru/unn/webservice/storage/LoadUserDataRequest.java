package ru.unn.webservice.storage;

public class LoadUserDataRequest implements IDataRequest {
    public String username;

    public LoadUserDataRequest(String username) {
        this.username = username;
    }
}
