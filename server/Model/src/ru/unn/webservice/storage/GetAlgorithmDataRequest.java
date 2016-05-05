package ru.unn.webservice.storage;

public class GetAlgorithmDataRequest implements IDataRequest {
    public String algorithm;

    public GetAlgorithmDataRequest() { }
    public GetAlgorithmDataRequest(String algorithm) {
        this.algorithm = algorithm;
    }
}
