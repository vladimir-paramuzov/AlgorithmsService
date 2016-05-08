package ru.unn.webservice.server;

import ru.unn.webservice.storage.Algorithm;

public class AddAlgorithmRequest implements IRequest {
    public Algorithm algorithm;

    public AddAlgorithmRequest(Algorithm algorithm) {
        this.algorithm = algorithm;
    }
}
