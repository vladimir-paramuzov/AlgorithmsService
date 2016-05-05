package ru.unn.webservice.storage;

public class GetUserDataResponse implements IDataResponse {
    public User user;
    public String status;

    public GetUserDataResponse(User user, String status) {
        this.user = user;
        this.status = status;
    }
}
