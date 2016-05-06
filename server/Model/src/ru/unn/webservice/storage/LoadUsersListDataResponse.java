package ru.unn.webservice.storage;

import java.util.ArrayList;

public class LoadUsersListDataResponse implements IDataResponse {
    public ArrayList<User> usersList;
    public String status;

    public LoadUsersListDataResponse(ArrayList<User> usersList, String status) {
        this.usersList = usersList;
        this.status = status;
    }
}
