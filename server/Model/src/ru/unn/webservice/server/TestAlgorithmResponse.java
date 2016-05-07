package ru.unn.webservice.server;

public class TestAlgorithmResponse implements IResponse {
    public byte[] log;
    public String status;

    public TestAlgorithmResponse(byte[] log, String status) {
        this.log = log;
        this.status = status;
    }
}
