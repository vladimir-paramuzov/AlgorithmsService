package ru.unn.webservice.server;

import ru.unn.webservice.storage.Algorithm;

public class DownloadResponse implements IResponse {
    public Algorithm algorithm;
    public String status;

    public DownloadResponse(Algorithm algorithm, String status) {
        this.algorithm = algorithm;
        this.status = status;
    }
}
