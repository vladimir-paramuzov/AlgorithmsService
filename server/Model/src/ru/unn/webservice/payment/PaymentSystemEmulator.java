package ru.unn.webservice.payment;

import ru.unn.webservice.server.ChangeBalanceRequest;
import ru.unn.webservice.server.ChangeBalanceResponse;
import ru.unn.webservice.server.IRequest;
import ru.unn.webservice.server.IResponse;
import ru.unn.webservice.storage.*;

public class PaymentSystemEmulator implements IPaymentSystem {
    public PaymentSystemEmulator(IDataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    @Override
    public ChangeBalanceResponse changeBalance(ChangeBalanceRequest request) {
        LoadUserDataResponse response = (LoadUserDataResponse)dataAccess.process(
                                         new LoadUserDataRequest(request.username));
        if (!response.status.equals("OK")) {
            return new ChangeBalanceResponse(response.status);
        }

        if (response.user.balance + request.delta < 0) {
            return new ChangeBalanceResponse("NOT ENOUGH MONEY");
        }

        response.user.balance += request.delta;

        String status = ((StoreUserDataResponse)dataAccess.process(new StoreUserDataRequest(response.user))).status;

        if (!status.equals("OK")) {
            return new ChangeBalanceResponse(status);
        }

        return new ChangeBalanceResponse("OK");
    }

    private IDataAccess dataAccess;

}
