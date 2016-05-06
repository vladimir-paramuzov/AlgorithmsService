package ru.unn.webservice.authorization;

import ru.unn.webservice.server.IResponse;

public class AuthorizationResponse implements IResponse {
    public String status;
    public AuthorizationResponse(String status) {
        this.status = status;
    }
}
