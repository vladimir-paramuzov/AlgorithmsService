package ru.unn.webservice.storage;

public class LoadAlgorithmDataRequest implements IDataRequest {
    public String algorithmName;

    public LoadAlgorithmDataRequest() { }
    public LoadAlgorithmDataRequest(String algorithmName) {
        this.algorithmName = algorithmName;
    }
}
