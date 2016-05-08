package ru.unn.webservice.storage;

import ru.unn.webservice.infrastructure.Statistic;

public class StoreStatDataRequest implements IDataRequest {
    Statistic statistic;

    public StoreStatDataRequest(Statistic statistic) {
        this.statistic = statistic;
    }
}
