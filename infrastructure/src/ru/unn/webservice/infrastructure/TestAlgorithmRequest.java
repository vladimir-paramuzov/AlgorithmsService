package ru.unn.webservice.infrastructure;

public class TestAlgorithmRequest implements IRequest {
    public String algorithmName;
    public byte[] userTestData;

    public TestAlgorithmRequest(String algorithmName, byte[] userTestData) {
        this.algorithmName = algorithmName;
        this.userTestData = userTestData;
    }
}
