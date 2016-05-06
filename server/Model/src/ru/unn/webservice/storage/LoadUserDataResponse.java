package ru.unn.webservice.storage;

public class LoadUserDataResponse implements IDataResponse {
    public User user;
    public String status;

    public LoadUserDataResponse(User user, String status) {
        this.user = user;
        this.status = status;
    }
}
