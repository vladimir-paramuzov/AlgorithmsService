package ru.unn.webservice.storage;

public class StoreStatDataResponse implements IDataResponse {
    public String status;

    public StoreStatDataResponse(String status) {
        this.status = status;
    }
}
