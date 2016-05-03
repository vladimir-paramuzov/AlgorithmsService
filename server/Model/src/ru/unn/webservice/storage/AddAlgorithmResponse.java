package ru.unn.webservice.storage;

public class AddAlgorithmResponse implements IDataResponse {
    public String status;

    public AddAlgorithmResponse() { }
    public AddAlgorithmResponse(String status) {
        this.status = status;
    }
}
