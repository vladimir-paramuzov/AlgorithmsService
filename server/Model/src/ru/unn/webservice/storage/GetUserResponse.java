package ru.unn.webservice.storage;

public class GetUserResponse implements IDataResponse {
    public User user;
    public String status;

    public GetUserResponse(User user, String status) {
        this.user = user;
        this.status = status;
    }
}
