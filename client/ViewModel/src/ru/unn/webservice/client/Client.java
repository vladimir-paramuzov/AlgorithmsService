package ru.unn.webservice.client;


import ru.unn.webservice.infrastructure.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Client implements IViewModel {
    private User user;
    private String login;
    private String password;
    private String searchField;
    private String showAlgorithmField;
    private boolean isDeveloper;
    private boolean isAuthorizationPanelVisible;
    private boolean isUserProfilePanelVisible;
    private boolean isStartPanelVisible;
    private boolean isInputPanelVisible;
    private boolean isDeveloperPanelVisible;
    private boolean isAdminPanelVisible;
    private Date from;
    private Date to;
    private ArrayList<String> searchResultList;
    private ArrayList<Algorithm> searchAlgorithmsList;
    private int selectedAlgorithmIndex;
    private boolean isSearchPanelVisible;


    private Statistic statistic;

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public Algorithm getAlgorithmToAdd() {
        return algorithmToAdd;
    }

    public void setAlgorithmToAdd(Algorithm algorithmToAdd) {
        this.algorithmToAdd = algorithmToAdd;
    }

    public Algorithm getAlgorithmToShow() {
        return algorithmToShow;
    }

    public void setAlgorithmToShow(Algorithm algorithmToShow) {
        this.algorithmToShow = algorithmToShow;
    }

    private Algorithm algorithmToAdd;
    private Algorithm algorithmToShow;

    public String getShowAlgorithmField() {
        return showAlgorithmField;
    }

    public void setShowAlgorithmField(String showAlgorithmField) {
        this.showAlgorithmField = showAlgorithmField;
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
            isDeveloperPanelVisible = user.type == User.TYPE.DEVELOPER || user.type == User.TYPE.ADMIN;
            isAdminPanelVisible = user.type == User.TYPE.ADMIN;
            isSearchPanelVisible = true;
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
        isSearchPanelVisible = false;
        isDeveloperPanelVisible = false;
        isAdminPanelVisible = false;
    }

    public void search() {
        ArrayList<String> tags = new ArrayList<>(Arrays.asList(searchField.split(" ")));
        SearchAlgorithmRequest request = new SearchAlgorithmRequest(tags);
        SearchAlgorithmResponse response = (SearchAlgorithmResponse)serverConnection.send(request);
        searchResultList.clear();

        searchAlgorithmsList = response.getAlgorithmsList();

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

    public void updateUser() {
        AuthorizationRequest request = new AuthorizationRequest(login, password);
        AuthorizationResponse response = (AuthorizationResponse)serverConnection.send(request);
        status = response.getStatus();
    }

    @Override
    public void addAlgorithm() {
        AddAlgorithmRequest request = new AddAlgorithmRequest(algorithmToAdd);
        AddAlgorithmResponse response = (AddAlgorithmResponse)serverConnection.send(request);
        status = response.getStatus();
    }

    @Override
    public void setIsDeveloperPanelVisible(boolean visible) {
        isDeveloperPanelVisible = visible;
    }

    @Override
    public void setisAdminPanelVisible(boolean visible) {
        isAdminPanelVisible = visible;
    }

    @Override
    public boolean getIsDeveloperPanelVisible() {
        return isDeveloperPanelVisible;
    }

    @Override
    public boolean getIsAdminPanelVisible() {
        return isAdminPanelVisible;
    }


    @Override
    public void getStatistic() {
        GetStatisticRequest request = new GetStatisticRequest(from, to);
        GetStatisticResponse response = (GetStatisticResponse)serverConnection.send(request);
        if (status.equals("OK")) {
            statistic = response.getStatistic();
            return;
        }
        status = response.getStatus();
    }

    @Override
    public String getDownloadsCount() {
        if (statistic == null) {
            return "";
        }
        return Integer.toString(statistic.getDownloadsCount());
    }

    @Override
    public String getPurchasesCount() {
        if (statistic == null) {
            return "";
        }
        return Integer.toString(statistic.getPurchasesCount());
    }

    @Override
    public String getStatisticDateLabel() {
        if (statistic == null) {
            return "";
        }
        return "<html>Stats from <br> <pre>    " + from.toString() + "</pre>to <br><pre>    " + to.toString() + "</pre>";
    }

    @Override
    public void setSelectedAlgorithmIndex(int selectedIndex) {
        selectedAlgorithmIndex = selectedIndex;
    }

    @Override
    public void getSelectedAlgorithm() {
        if (selectedAlgorithmIndex >= 0 && selectedAlgorithmIndex < searchAlgorithmsList.size()) {
            Algorithm algorithm = searchAlgorithmsList.get(selectedAlgorithmIndex);
            showAlgorithmField =
                    "<html>" +
                    "Name: <br> <pre>    " + algorithm.name + "</pre>" +
                    "Description: <br><pre>    " + algorithm.description + "</pre>" +
                    "Cost: <br><pre>    " + Integer.toString(algorithm.cost) + "</pre>" +
                    "Owner: <br><pre>    " + algorithm.owner + "</pre>" +
                    "Tags: <br><pre>    " + algorithm.tags.toString() + "</pre>";
        }
    }

    @Override
    public void setIsSearchPanelVisible(boolean visible) {
        isSearchPanelVisible = visible;
    }

    @Override
    public boolean getIsSearchPanelVisible() {
        return isSearchPanelVisible;
    }
}
