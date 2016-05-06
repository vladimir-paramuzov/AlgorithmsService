package ru.unn.webservice.storage;

public class LoadAlgorithmDataResponse implements IDataResponse {
    public Algorithm algorithm;
    public String status;

    public LoadAlgorithmDataResponse(Algorithm algorithm, String status) {
        this.algorithm = algorithm;
        this.status = status;
    }
}
