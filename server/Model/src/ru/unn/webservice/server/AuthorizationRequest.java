package ru.unn.webservice.server;

import ru.unn.webservice.server.IRequest;

public class AuthorizationRequest implements IRequest {
    public String login;
    public String pass;

    public AuthorizationRequest(String login, String pass) {
        this.login = login;
        this.pass = pass;
    }
}
