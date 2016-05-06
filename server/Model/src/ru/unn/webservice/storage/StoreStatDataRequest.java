package ru.unn.webservice.storage;

public class StoreStatDataRequest implements IDataRequest {
    Statistic statistic;

    public StoreStatDataRequest(Statistic statistic) {
        this.statistic = statistic;
    }
}
