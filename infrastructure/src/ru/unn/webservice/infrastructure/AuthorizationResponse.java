package ru.unn.webservice.infrastructure;

public class AuthorizationResponse implements IResponse {
    public User user;
    public String status;

    public AuthorizationResponse(User user, String status) {
        this.user = user;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
