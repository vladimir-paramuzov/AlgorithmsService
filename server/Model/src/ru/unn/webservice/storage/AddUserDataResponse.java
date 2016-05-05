package ru.unn.webservice.storage;

public class AddUserDataResponse implements IDataResponse {
    public String status;

    public AddUserDataResponse() {   }
    public AddUserDataResponse(String status) {
        this.status = status;
    }
}
