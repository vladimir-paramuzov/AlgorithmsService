package ru.unn.webservice.storage;

import ru.unn.webservice.infrastructure.User;

import java.util.ArrayList;

public class LoadUsersListDataResponse implements IDataResponse {
    public ArrayList<User> usersList;
    public String status;

    public LoadUsersListDataResponse(ArrayList<User> usersList, String status) {
        this.usersList = usersList;
        this.status = status;
    }
}
