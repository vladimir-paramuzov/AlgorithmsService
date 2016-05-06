package ru.unn.webservice.server;

public class RegisterResponse implements IResponse {
    public String status;
    public RegisterResponse(String status) {
        this.status = status;
    }
}

