package ru.unn.webservice.storage;

import java.util.ArrayList;

public class AddAlgorithmRequest implements IDataRequest{
    public Algorithm algorithm;

    public AddAlgorithmRequest(Algorithm algorithm) {
        this.algorithm = algorithm;
    }
}
