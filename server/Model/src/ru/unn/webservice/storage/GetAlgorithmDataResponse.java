package ru.unn.webservice.storage;

public class GetAlgorithmDataResponse implements IDataResponse {
    public Algorithm algorithm;
    public String status;

    public GetAlgorithmDataResponse(Algorithm algorithm, String status) {
        this.algorithm = algorithm;
        this.status = status;
    }
}
