package ru.unn.webservice.infrastructure;

public class DownloadAlgorithmRequest implements IRequest {
    public String userName;
    public String algorithmName;

    public DownloadAlgorithmRequest(String userName, String algorithmName) {
        this.userName = userName;
        this.algorithmName = algorithmName;
    }
}
