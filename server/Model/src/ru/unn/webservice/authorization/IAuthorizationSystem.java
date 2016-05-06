package ru.unn.webservice.authorization;

import ru.unn.webservice.server.IRequest;
import ru.unn.webservice.server.IResponse;

public interface IAuthorizationSystem {
    IResponse process(IRequest request);
}
