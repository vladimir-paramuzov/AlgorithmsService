package ru.unn.webservice.storage;

import ru.unn.webservice.infrastructure.Algorithm;

public class StoreAlgorithmDataRequest implements IDataRequest{
    public Algorithm algorithm;

    public StoreAlgorithmDataRequest(Algorithm algorithm) {
        this.algorithm = algorithm;
    }
}
