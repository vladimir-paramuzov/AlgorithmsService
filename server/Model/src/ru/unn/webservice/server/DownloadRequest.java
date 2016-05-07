package ru.unn.webservice.server;

public class DownloadRequest implements IRequest {
    public String username;
    public String algorithmName;

    public DownloadRequest(String username, String algorithmName) {
        this.username = username;
        this.algorithmName = algorithmName;
    }
}
