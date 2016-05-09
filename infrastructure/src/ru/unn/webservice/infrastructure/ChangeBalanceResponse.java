package ru.unn.webservice.infrastructure;

public class ChangeBalanceResponse implements IResponse {
    public User getUser() {
        return user;
    }

    public String getStatus() {
        return status;
    }

    private User user;
    private String status;

    public ChangeBalanceResponse(User user, String status) {
        this.user = user;
        this.status = status;
    }
}
