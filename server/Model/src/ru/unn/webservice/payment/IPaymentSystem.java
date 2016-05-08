package ru.unn.webservice.payment;

import ru.unn.webservice.infrastructure.IRequest;
import ru.unn.webservice.infrastructure.IResponse;

public interface IPaymentSystem {
    IResponse process(IRequest request);
}
