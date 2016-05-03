package ru.unn.webservice.storage;

public class GetAlgorithmResponse implements IDataResponse {
    public Algorithm algorithm;
    public String status;

    public GetAlgorithmResponse(Algorithm algorithm, String status) {
        this.algorithm = algorithm;
        this.status = status;
    }
}
