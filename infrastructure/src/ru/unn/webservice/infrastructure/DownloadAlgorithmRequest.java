package ru.unn.webservice.infrastructure;

public class DownloadAlgorithmRequest implements IRequest {
    public String username;
    public String algorithmName;

    public DownloadAlgorithmRequest(String username, String algorithmName) {
        this.username = username;
        this.algorithmName = algorithmName;
    }
}
