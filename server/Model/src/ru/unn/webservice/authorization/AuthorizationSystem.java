package ru.unn.webservice.authorization;

import ru.unn.webservice.infrastructure.*;
import ru.unn.webservice.storage.*;

import java.util.ArrayList;

public class AuthorizationSystem implements IAuthorizationSystem {
    public AuthorizationSystem(IDataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    @Override

    public IResponse process(IRequest request) {
        if (request instanceof AuthorizationRequest) {
            return (IResponse)authorize((AuthorizationRequest) request);
        } else if (request instanceof RegisterRequest){
            return (IResponse)register((RegisterRequest)request);
        } else {
            return null;
        }
    }

    private AuthorizationResponse authorize(AuthorizationRequest request) {
        LoadUserDataResponse response = (LoadUserDataResponse)dataAccess.process(new LoadUserDataRequest(request.login));

        if (!response.status.equals("OK")) {
            return new AuthorizationResponse(null, "WRONG LOGIN");
        }

        if(response.user.pass.equals(request.pass)) {
            return new AuthorizationResponse(response.user, "OK");
        }

        return new AuthorizationResponse(null, "WRONG PASS");
    }

    private RegisterResponse register(RegisterRequest request) {
        LoadUserDataResponse response = (LoadUserDataResponse)dataAccess.process(new LoadUserDataRequest(request.login));

        if (response.status.equals("OK")) {
            return new RegisterResponse("USER ALREADY EXISTS");
        }

        User user = new User(request.login, request.pass, request.type, 0, new ArrayList<>());
        String status = ((StoreUserDataResponse)dataAccess.process(new StoreUserDataRequest(user))).status;

        if (!status.equals("OK")) {
            return new RegisterResponse(status);
        }

        return new RegisterResponse("OK");
    }

    private IDataAccess dataAccess;
}
