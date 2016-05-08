package ru.unn.webservice.infrastructure;

public class AddAlgorithmRequest implements IRequest {
    public Algorithm algorithm;

    public AddAlgorithmRequest(Algorithm algorithm) {
        this.algorithm = algorithm;
    }
}
