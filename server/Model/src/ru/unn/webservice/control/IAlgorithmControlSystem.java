package ru.unn.webservice.control;

import ru.unn.webservice.server.IRequest;
import ru.unn.webservice.server.IResponse;

public interface IAlgorithmControlSystem {
    IResponse process(IRequest request);
}
