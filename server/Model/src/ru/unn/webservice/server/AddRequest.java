package ru.unn.webservice.server;

import ru.unn.webservice.storage.Algorithm;

public class AddRequest implements IRequest {
    public Algorithm algorithm;

    public AddRequest(Algorithm algorithm) {
        this.algorithm = algorithm;
    }
}
