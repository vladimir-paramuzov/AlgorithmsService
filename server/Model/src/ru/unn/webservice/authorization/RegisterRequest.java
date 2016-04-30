package ru.unn.webservice.authorization;

import ru.unn.webservice.storage.IDataRequest;

public class RegisterRequest implements IDataRequest {
    public String login;
    public String pass;
}
