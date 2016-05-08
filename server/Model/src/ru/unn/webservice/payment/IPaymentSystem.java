package ru.unn.webservice.payment;

import ru.unn.webservice.server.ChangeBalanceRequest;
import ru.unn.webservice.server.ChangeBalanceResponse;
import ru.unn.webservice.server.IRequest;
import ru.unn.webservice.server.IResponse;

public interface IPaymentSystem {
    IResponse process(IRequest request);
}
