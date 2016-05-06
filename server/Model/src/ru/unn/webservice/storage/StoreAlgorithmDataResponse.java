package ru.unn.webservice.storage;

public class StoreAlgorithmDataResponse implements IDataResponse {
    public String status;

    public StoreAlgorithmDataResponse() { }
    public StoreAlgorithmDataResponse(String status) {
        this.status = status;
    }
}
