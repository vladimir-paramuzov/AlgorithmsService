package ru.unn.webservice.client;

import ru.unn.webservice.infrastructure.Algorithm;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;
import java.util.Date;

public interface IViewModel {

    String getShowAlgorithmField();

    void setShowAlgorithmField(String showAlgorithmField);

    String getSearchField();

    void setSearchField(String searchField);

    boolean getIsStartPanelVisible();

    void setIsStartPanelVisible(boolean startPanelVisible);

    boolean getIsInputPanelVisible();

    void setIsInputPanelVisible(boolean inputPanelVisible);

    String getStatus();

    void setStatus(String status);

    boolean getIsAuthorizationPanelVisible();

    void setAuthorizationPanelVisible(boolean authorizationPanelVisible);

    boolean getIsUserProfilePanelVisible();

    void setIsUserProfilePanelVisible(boolean userProfilePanelVisible);

    String getUserName();

    String getUserBalance();

    String getUserType();

    String getLogin();

    void setLogin(String login);

    String getPassword();

    void setPassword(String password);

    boolean getIsDeveloper();

    void setIsDeveloper(boolean developer);

    void register();

    void authorize();

    void refillBalance(String sum);

    void logout();

    void search();

    void setSearchResultList(ListModel model);

    ListModel<String> getSearchResultList();

    void updateUser();

    Algorithm getAlgorithmToAdd();

    void setAlgorithmToAdd(Algorithm algorithmToAdd);

    Algorithm getAlgorithmToShow();

    void setAlgorithmToShow(Algorithm algorithmToShow);

    static byte[] getBytes(Object obj) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            bos.close();
            return bos.toByteArray();
        }
        catch (java.io.IOException ex) {
            return null;
        }
    }

    void addAlgorithm();

    void setIsDeveloperPanelVisible(boolean visible);

    void setisAdminPanelVisible(boolean visible);

    boolean getIsDeveloperPanelVisible();

    boolean getIsAdminPanelVisible();

    Date getFrom();

    void setFrom(Date from);

    Date getTo();

    void setTo(Date to);

    void getStatistic();

    String getDownloadsCount();
    String getPurchasesCount();
    String getStatisticDateLabel();

    void setSelectedAlgorithmIndex(int selectedIndex);

    void getSelectedAlgorithm();

    void setIsSearchPanelVisible(boolean visible);

    boolean getIsSearchPanelVisible();

    boolean getCanDownload();
    void setCanDownload(boolean canDownload);

    boolean downloadAlgorithm();
    void saveAlgorithm(File directory);

    void buyAlgorithm();

    void setIsSearchResultPanelVisible(boolean visible);

    void setIsAlgorithmPanelVisible(boolean visible);
    boolean getIsSearchResultPanelVisible();

    boolean getIsAlgorithmPanelVisible();

    void setUserData(File selectedFile);

    void testAlgorithm();

    void setLogField(String text);

    String getLogField();
}
