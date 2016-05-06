package ru.unn.webservice.server;

import ru.unn.webservice.storage.User;

public class RegisterRequest implements IRequest {
    public String login;
    public String pass;
    public User.TYPE type;

    public RegisterRequest(String login, String pass, User.TYPE type) {
        this.login = login;
        this.pass = pass;
        this.type = type;
    }
}
