package ru.unn.webservice.storage;

public class AddUserDataRequest implements IDataRequest {
    public User user;

    public AddUserDataRequest(User user) {
        this.user = user;
    }
}
