package ru.unn.webservice.storage;

public class StoreUserDataResponse implements IDataResponse {
    public String status;

    public StoreUserDataResponse() {   }
    public StoreUserDataResponse(String status) {
        this.status = status;
    }
}
