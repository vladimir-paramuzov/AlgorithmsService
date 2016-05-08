package ru.unn.webservice.client;


import ru.unn.webservice.infrastructure.*;

public class Client implements IViewModel {
    private String login;
    private String password;
    private boolean isDeveloper;
    private String authorizationStatus;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getIsDeveloper() {
        return isDeveloper;
    }

    public void setIsDeveloper(boolean developer) {
        isDeveloper = developer;
    }

    public Client() {
        serverConnection = new ServerConnection();
    }

    @Override
    public void send() {
        AuthorizationRequest request = new AuthorizationRequest("testUser", "user");
        AuthorizationResponse response = (AuthorizationResponse)serverConnection.send(request);
        System.out.println(response.user.login);
    }

    ServerConnection serverConnection;

    public void register() {
        RegisterRequest request = new RegisterRequest(login, password, isDeveloper ? User.TYPE.DEVELOPER : User.TYPE.USER);
        RegisterResponse response = (RegisterResponse)serverConnection.send(request);
        authorizationStatus = response.status;
    }

    public void authorize() {
        AuthorizationRequest request = new AuthorizationRequest(login, password);
        AuthorizationResponse response = (AuthorizationResponse)serverConnection.send(request);
        authorizationStatus = response.status;
    }

    public String getAuthorizationStatus() {
        return authorizationStatus;
    }
}
