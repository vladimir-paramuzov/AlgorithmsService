package ru.unn.webservice.storage;

public class GetUserRequest implements IDataRequest {
    public User user;

    public GetUserRequest(User user) {
        this.user = user;
    }
}
