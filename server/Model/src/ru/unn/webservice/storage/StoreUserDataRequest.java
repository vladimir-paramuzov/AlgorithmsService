package ru.unn.webservice.storage;

public class StoreUserDataRequest implements IDataRequest {
    public User user;

    public StoreUserDataRequest(User user) {
        this.user = user;
    }
}
