package ru.unn.webservice.authorization;

import ru.unn.webservice.infrastructure.IRequest;
import ru.unn.webservice.infrastructure.IResponse;

public interface IAuthorizationSystem {
    IResponse process(IRequest request);
}
