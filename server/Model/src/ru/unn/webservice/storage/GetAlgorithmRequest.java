package ru.unn.webservice.storage;

public class GetAlgorithmRequest implements IDataRequest {
    public Algorithm algorithm;

    public GetAlgorithmRequest() { }
    public GetAlgorithmRequest(Algorithm algorithm) {
        this.algorithm = algorithm;
    }
}
