package ru.unn.webservice.control;

import ru.unn.webservice.infrastructure.IRequest;
import ru.unn.webservice.infrastructure.IResponse;

public interface IAlgorithmControlSystem {
    IResponse process(IRequest request);
}
