package ru.unn.webservice.payment;

import ru.unn.webservice.infrastructure.ChangeBalanceRequest;
import ru.unn.webservice.infrastructure.ChangeBalanceResponse;
import ru.unn.webservice.infrastructure.IRequest;
import ru.unn.webservice.infrastructure.IResponse;
import ru.unn.webservice.storage.*;

public class PaymentSystemEmulator implements IPaymentSystem {
    public PaymentSystemEmulator(IDataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    @Override
    public IResponse process(IRequest request) {
        if (request instanceof ChangeBalanceRequest) {
            return changeBalance((ChangeBalanceRequest)request);
        } else {
            return null;
        }
    }

    private ChangeBalanceResponse changeBalance(ChangeBalanceRequest request) {
        LoadUserDataResponse response = (LoadUserDataResponse)dataAccess.process(
                new LoadUserDataRequest(request.username));
        if (!response.status.equals("OK")) {
            return new ChangeBalanceResponse(null, response.status);
        }

        if (response.user.balance + request.delta < 0) {
            return new ChangeBalanceResponse(null, "NOT ENOUGH MONEY");
        }

        response.user.balance += request.delta;

        String status = ((StoreUserDataResponse)dataAccess.process(new StoreUserDataRequest(response.user))).status;

        if (!status.equals("OK")) {
            return new ChangeBalanceResponse(null, status);
        }

        return new ChangeBalanceResponse(response.user, "OK");
    }

    private IDataAccess dataAccess;

}
