package ru.unn.webservice.storage;

import ru.unn.webservice.infrastructure.User;

public class StoreUserDataRequest implements IDataRequest {
    public User user;

    public StoreUserDataRequest(User user) {
        this.user = user;
    }
}
