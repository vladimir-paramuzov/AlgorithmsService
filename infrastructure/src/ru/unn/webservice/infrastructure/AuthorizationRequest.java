package ru.unn.webservice.infrastructure;

public class AuthorizationRequest implements IRequest {
    public String login;
    public String pass;

    public AuthorizationRequest(String login, String pass) {
        this.login = login;
        this.pass = pass;
    }
}
