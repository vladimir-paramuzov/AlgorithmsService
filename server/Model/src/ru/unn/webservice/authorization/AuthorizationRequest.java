package ru.unn.webservice.authorization;

import ru.unn.webservice.server.IRequest;

public class AuthorizationRequest implements IRequest {
    public String login;
    public String pass;
}
