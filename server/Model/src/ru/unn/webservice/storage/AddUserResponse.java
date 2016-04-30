package ru.unn.webservice.storage;

public class AddUserResponse implements IDataResponse {
    public String status;

    public AddUserResponse() {

    }

    public AddUserResponse(String status) {
        this.status = status;
    }
}
