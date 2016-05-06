package ru.unn.webservice.payment;

import ru.unn.webservice.server.ChangeBalanceRequest;
import ru.unn.webservice.server.ChangeBalanceResponse;

public interface IPaymentSystem {
    ChangeBalanceResponse changeBalance(ChangeBalanceRequest request);
}
