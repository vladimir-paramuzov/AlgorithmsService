package ru.unn.webservice.storage;

public class AddUserRequest implements IDataRequest {
    public User user;

    public AddUserRequest(User user) {
        this.user = user;
    }
}
