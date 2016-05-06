package ru.unn.webservice.server;

import ru.unn.webservice.server.IResponse;
import ru.unn.webservice.storage.User;

public class AuthorizationResponse implements IResponse {
    public User user;
    public String status;

    public AuthorizationResponse(User user, String status) {
        this.user = user;
        this.status = status;
    }
}
