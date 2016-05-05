package ru.unn.webservice.storage;

import java.util.ArrayList;

public class AddAlgorithmDataRequest implements IDataRequest{
    public Algorithm algorithm;

    public AddAlgorithmDataRequest(Algorithm algorithm) {
        this.algorithm = algorithm;
    }
}
