package ru.unn.webservice.client;


import ru.unn.webservice.infrastructure.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

import static ru.unn.webservice.infrastructure.Language.CPP;

public class Client implements IViewModel {
    private User user;
    private String login;
    private String password;
    private String searchField;
    private String browserField;
    private boolean isDeveloper;
    private boolean isAuthorizationPanelVisible;
    private boolean isUserProfilePanelVisible;
    private boolean isStartPanelVisible;
    private boolean isInputPanelVisible;
    private ArrayList<String> searchResultList;

    public String getBrowserField() {
        return browserField;
    }

    public void setBrowserField(String browserField) {
        this.browserField = browserField;
    }

    public String getSearchField() {
        return searchField;
    }

    public void setSearchField(String searchField) {
        this.searchField = searchField;
    }

    public boolean getIsStartPanelVisible() {
        return isStartPanelVisible;
    }

    public void setIsStartPanelVisible(boolean startPanelVisible) {
        isStartPanelVisible = startPanelVisible;
    }

    public boolean getIsInputPanelVisible() {
        return isInputPanelVisible;
    }

    public void setIsInputPanelVisible(boolean inputPanelVisible) {
        isInputPanelVisible = inputPanelVisible;
    }

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean getIsAuthorizationPanelVisible() {
        return isAuthorizationPanelVisible;
    }

    public void setAuthorizationPanelVisible(boolean authorizationPanelVisible) {
        isAuthorizationPanelVisible = authorizationPanelVisible;
    }

    public boolean getIsUserProfilePanelVisible() {
        return isUserProfilePanelVisible;
    }

    public void setIsUserProfilePanelVisible(boolean userProfilePanelVisible) {
        isUserProfilePanelVisible = userProfilePanelVisible;
    }

    public String getUserName() {
        if (user == null) {
            return "";
        }

        return user.login;
    }

    public String getUserBalance() {
        if (user == null) {
            return "";
        }

        return Integer.toString(user.balance);
    }

    public String getUserType() {
        if (user == null) {
            return "";
        }

        return user.type.toString();
    }


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

    ServerConnection serverConnection;

    public void register() {
        RegisterRequest request = new RegisterRequest(login, password, isDeveloper ? User.TYPE.DEVELOPER : User.TYPE.USER);
        RegisterResponse response = (RegisterResponse)serverConnection.send(request);

        if (response.getStatus().equals("OK")) {
            status = "";
            return;
        }

        status = response.getStatus();
    }

    public void authorize() {
        AuthorizationRequest request = new AuthorizationRequest(login, password);
        AuthorizationResponse response = (AuthorizationResponse)serverConnection.send(request);
        if (response.getStatus().equals("OK")) {
            user = response.user;
            isAuthorizationPanelVisible = false;
            isUserProfilePanelVisible = true;
            status = "";
            return;
        }

        status = response.getStatus();
    }

    public void refillBalance(String sum) {
        int refillSum;
        try {
            refillSum = Integer.parseInt(sum);
            if (refillSum <= 0) {
                status = "Invalid sum";
                return;
            }
        } catch (Exception ex) {
            status = "Invalid sum";
            return;
        }

        ChangeBalanceRequest request = new ChangeBalanceRequest(refillSum, user.login);
        ChangeBalanceResponse response = (ChangeBalanceResponse)serverConnection.send(request);

        if (response.getStatus().equals("OK")) {
            user = response.getUser();
            status = "";
            return;
        }

        status = response.getStatus();
    }

    public void logout() {
        user = null;
        isAuthorizationPanelVisible = true;
        isUserProfilePanelVisible = false;
        isInputPanelVisible = false;
        isStartPanelVisible = true;
    }

    public void search() {
        ArrayList<String> tags = new ArrayList<String>(Arrays.asList(searchField.split(" ")));
        SearchAlgorithmRequest request = new SearchAlgorithmRequest(tags);
        SearchAlgorithmResponse response = (SearchAlgorithmResponse)serverConnection.send(request);
        searchResultList.clear();

        if (response.getStatus().equals("OK")) {
            for (Algorithm algorithm : response.getAlgorithmsList()) {
                searchResultList.add(algorithm.name);
            }
            status = "";
            return;
        }

        status = response.getStatus();
    }

    public void setSearchResultList(ListModel model) {
        searchResultList = new ArrayList<>();
        for (int i = 0; i < model.getSize(); i++) {
            searchResultList.add((String)model.getElementAt(i));
        }
    }

    public ListModel<String> getSearchResultList(){
        DefaultListModel<String> model = new DefaultListModel<>();
        searchResultList.forEach(model::addElement);

        return model;
    }
}
