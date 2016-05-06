package ru.unn.webservice.authorization;

import ru.unn.webservice.storage.IDataResponse;

public class RegisterResponse implements IDataResponse {
    public String status;
    public RegisterResponse(String status) {
        this.status = status;
    }
}

