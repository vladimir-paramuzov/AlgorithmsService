package ru.unn.webservice.infrastructure;

public class DownloadAlgorithmResponse implements IResponse {
    public Algorithm algorithm;
    public String status;

    public DownloadAlgorithmResponse(Algorithm algorithm, String status) {
        this.algorithm = algorithm;
        this.status = status;
    }
}
