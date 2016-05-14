package ru.unn.webservice.client;


import ru.unn.webservice.infrastructure.*;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Client implements IViewModel {
    ServerConnection serverConnection;
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
    private boolean canDownload;
    private Statistic statistic;
    private Algorithm algorithmToAdd;
    private Algorithm algorithmToShow;
    private Algorithm algorithmToSave;
    private String status;
    private boolean isSearchResultPanelVisible;
    private boolean isAlgorithmPanelVisible;
    private byte[] userData;
    private String logField;

    public Client() {
        serverConnection = new ServerConnection();
    }

    public Client(String ip) throws Exception {
        serverConnection = new ServerConnection(ip);
    }

    @Override
    public void register() {
        RegisterRequest request = new RegisterRequest(login, password, isDeveloper ? User.TYPE.DEVELOPER : User.TYPE.USER);
        RegisterResponse response = (RegisterResponse)serverConnection.send(request);

        if (response.getStatus().equals("OK")) {
            status = "";
            isInputPanelVisible = false;
            isStartPanelVisible = true;
            return;
        }

        status = response.getStatus();
    }

    @Override
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

    @Override
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

    @Override
    public void logout() {
        user = null;
        isAuthorizationPanelVisible = true;
        isUserProfilePanelVisible = false;
        isInputPanelVisible = false;
        isStartPanelVisible = true;
        isSearchPanelVisible = false;
        isDeveloperPanelVisible = false;
        isAdminPanelVisible = false;
        login = "";
        searchField = "";
        showAlgorithmField = "";
        password = "";
        isDeveloper = false;
        from = null;
        to = null;
        searchResultList = new ArrayList<>();
        searchAlgorithmsList = new ArrayList<>();
        selectedAlgorithmIndex = -1;
        canDownload = false;
        statistic = new Statistic();
        algorithmToAdd = null;
        algorithmToShow = null;
        algorithmToSave = null;
        status = "";
        isAlgorithmPanelVisible = false;
        isSearchResultPanelVisible = true;
        logField = "";
    }

    @Override
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

    @Override
    public void updateUser() {
        AuthorizationRequest request = new AuthorizationRequest(login, password);
        AuthorizationResponse response = (AuthorizationResponse)serverConnection.send(request);
        user = response.user;
    }

    @Override
    public void addAlgorithm() {
        AddAlgorithmRequest request = new AddAlgorithmRequest(algorithmToAdd);
        AddAlgorithmResponse response = (AddAlgorithmResponse)serverConnection.send(request);
        status = response.getStatus();
    }

    @Override
    public void getSelectedAlgorithm() {
        if (selectedAlgorithmIndex >= 0 && selectedAlgorithmIndex < searchAlgorithmsList.size()) {
            algorithmToShow = searchAlgorithmsList.get(selectedAlgorithmIndex);
            showAlgorithmField =
                    "<html>" +
                    "Name: <br> <pre>    " + algorithmToShow.name + "</pre>" +
                    "Description: <br><pre>    " + algorithmToShow.description + "</pre>" +
                    "Cost: <br><pre>    " + Integer.toString(algorithmToShow.cost) + "</pre>" +
                    "Owner: <br><pre>    " + algorithmToShow.owner + "</pre>" +
                    "Tags: <br><pre>    " + algorithmToShow.tags.toString() + "</pre>";
            canDownload = !(algorithmToShow.cost > 0 &&
                          !isAlgorithmBought(user, algorithmToShow.name) &&
                          !algorithmToShow.owner.equals(user.login));
        }
    }

    @Override
    public boolean downloadAlgorithm() {
        DownloadAlgorithmRequest request = new DownloadAlgorithmRequest(user.login, algorithmToShow.name);
        DownloadAlgorithmResponse response = (DownloadAlgorithmResponse)serverConnection.send(request);
        if (response.status.equals("OK")) {
            algorithmToSave = response.algorithm;
            return true;
        }
        status = response.status;
        return false;
    }

    @Override
    public void saveAlgorithm(File directory) {
        Path path = Paths.get(directory.getAbsolutePath()).normalize().resolve(algorithmToSave.name);
        File dir = new File(path.toString());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        writeAlgorithm(path);
    }

    @Override
    public void buyAlgorithm() {
        BuyAlgorithmRequest request = new BuyAlgorithmRequest(user.login, algorithmToShow.name);
        BuyAlgorithmResponse response = (BuyAlgorithmResponse)serverConnection.send(request);

        if (response.status.equals("OK")) {
            updateUser();
            canDownload = !(algorithmToShow.cost > 0 &&
                    !isAlgorithmBought(user, algorithmToShow.name) &&
                    !algorithmToShow.owner.equals(user.login));
            return;
        }
        status = response.status;
    }

    @Override
    public boolean getCanDownload() {
        return canDownload;
    }

    @Override
    public void setCanDownload(boolean canDownload) {
        this.canDownload = canDownload;
    }

    @Override
    public Date getFrom() {
        return from;
    }

    @Override
    public void setFrom(Date from) {
        this.from = from;
    }

    @Override
    public Date getTo() {
        return to;
    }

    @Override
    public void setTo(Date to) {
        this.to = to;
    }

    @Override
    public Algorithm getAlgorithmToAdd() {
        return algorithmToAdd;
    }

    @Override
    public void setAlgorithmToAdd(Algorithm algorithmToAdd) {
        this.algorithmToAdd = algorithmToAdd;
    }

    @Override
    public Algorithm getAlgorithmToShow() {
        return algorithmToShow;
    }

    @Override
    public void setAlgorithmToShow(Algorithm algorithmToShow) {
        this.algorithmToShow = algorithmToShow;
    }

    @Override
    public String getShowAlgorithmField() {
        return showAlgorithmField;
    }

    @Override
    public void setShowAlgorithmField(String showAlgorithmField) {
        this.showAlgorithmField = showAlgorithmField;
    }

    @Override
    public String getSearchField() {
        return searchField;
    }

    @Override
    public void setSearchField(String searchField) {
        this.searchField = searchField;
    }

    @Override
    public boolean getIsStartPanelVisible() {
        return isStartPanelVisible;
    }

    @Override
    public void setIsStartPanelVisible(boolean startPanelVisible) {
        isStartPanelVisible = startPanelVisible;
    }

    @Override
    public boolean getIsInputPanelVisible() {
        return isInputPanelVisible;
    }

    @Override
    public void setIsInputPanelVisible(boolean inputPanelVisible) {
        isInputPanelVisible = inputPanelVisible;
    }


    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean getIsAuthorizationPanelVisible() {
        return isAuthorizationPanelVisible;
    }

    @Override
    public void setAuthorizationPanelVisible(boolean authorizationPanelVisible) {
        isAuthorizationPanelVisible = authorizationPanelVisible;
    }

    @Override
    public boolean getIsUserProfilePanelVisible() {
        return isUserProfilePanelVisible;
    }

    @Override
    public void setIsUserProfilePanelVisible(boolean userProfilePanelVisible) {
        isUserProfilePanelVisible = userProfilePanelVisible;
    }

    @Override
    public String getUserName() {
        if (user == null) {
            return "";
        }

        return user.login;
    }

    @Override
    public String getUserBalance() {
        if (user == null) {
            return "";
        }

        return Integer.toString(user.balance);
    }

    @Override
    public String getUserType() {
        if (user == null) {
            return "";
        }

        return user.type.toString();
    }

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean getIsDeveloper() {
        return isDeveloper;
    }

    @Override
    public void setIsDeveloper(boolean developer) {
        isDeveloper = developer;
    }

    @Override
    public void setSearchResultList(ListModel model) {
        searchResultList = new ArrayList<>();
        for (int i = 0; i < model.getSize(); i++) {
            searchResultList.add((String)model.getElementAt(i));
        }
    }

    @Override
    public ListModel<String> getSearchResultList(){
        DefaultListModel<String> model = new DefaultListModel<>();
        searchResultList.forEach(model::addElement);

        return model;
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
        if (response.getStatus().equals("OK")) {
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
        if (statistic == null || from == null || to == null) {
            return "";
        }
        return "<html>Stats from <br> <pre>    " + from.toString() + "</pre>to <br><pre>    " + to.toString() + "</pre>";
    }

    @Override
    public void setSelectedAlgorithmIndex(int selectedIndex) {
        selectedAlgorithmIndex = selectedIndex;
    }

    @Override
    public void setIsSearchPanelVisible(boolean visible) {
        isSearchPanelVisible = visible;
    }

    @Override
    public boolean getIsSearchPanelVisible() {
        return isSearchPanelVisible;
    }

    @Override
    public void setIsSearchResultPanelVisible(boolean visible) {
        isSearchResultPanelVisible = visible;
    }

    @Override
    public boolean getIsSearchResultPanelVisible() {
        return isSearchResultPanelVisible;
    }

    @Override
    public boolean getIsAlgorithmPanelVisible() {
        return isAlgorithmPanelVisible;
    }

    @Override
    public void setIsAlgorithmPanelVisible(boolean visible) {
        isAlgorithmPanelVisible = visible;
    }

    @Override
    public void setUserData(File selectedFile) {
        if(selectedFile == null) {
            userData = null;
            return;
        }

        if (selectedFile.exists()) {
            try {
                userData = Files.readAllBytes(Paths.get(selectedFile.getPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void testAlgorithm() {
        TestAlgorithmRequest request = new TestAlgorithmRequest(algorithmToShow.name, userData);
        TestAlgorithmResponse response = (TestAlgorithmResponse)serverConnection.send(request);

        try {
            logField = new String(response.log, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getLogField() {
        return logField;
    }

    @Override
    public void setLogField(String logField) {
        this.logField = logField;
    }

    private boolean isAlgorithmBought(User user, String selectedAlgorithmName) {
        for (String algorithmName : user.purchasedAlgorithms) {
            if (algorithmName.equals(selectedAlgorithmName)) {
                return true;
            }
        }
        return false;
    }

    private void writeAlgorithm(Path algorithmDirectory) {
        try {
            String decodedSource = new String(algorithmToSave.sourceFile, "UTF-8");
            BufferedWriter writer = new BufferedWriter(new FileWriter(
                    algorithmDirectory.resolve("main.cpp").toString()));
            writer.write(decodedSource);
            writer.flush();
            writer.close();
            String decodedTests = new String(algorithmToSave.testFile, "UTF-8");
            writer = new BufferedWriter(new FileWriter(algorithmDirectory.resolve("test_data.txt").toString()));
            writer.write(decodedTests);
            writer.flush();
            writer.close();
        } catch (Exception ex) {
            status = "Save error";
        }
    }
}
