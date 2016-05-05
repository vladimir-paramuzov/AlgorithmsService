package ru.unn.webservice.storage;

public class AddAlgorithmDataResponse implements IDataResponse {
    public String status;

    public AddAlgorithmDataResponse() { }
    public AddAlgorithmDataResponse(String status) {
        this.status = status;
    }
}
