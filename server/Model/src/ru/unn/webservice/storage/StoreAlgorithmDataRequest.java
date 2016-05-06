package ru.unn.webservice.storage;

import java.util.ArrayList;

public class StoreAlgorithmDataRequest implements IDataRequest{
    public Algorithm algorithm;

    public StoreAlgorithmDataRequest(Algorithm algorithm) {
        this.algorithm = algorithm;
    }
}
