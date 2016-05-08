package ru.unn.webservice.storage;

import ru.unn.webservice.infrastructure.User;

public class LoadUserDataResponse implements IDataResponse {
    public User user;
    public String status;

    public LoadUserDataResponse(User user, String status) {
        this.user = user;
        this.status = status;
    }
}
