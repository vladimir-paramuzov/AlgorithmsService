package ru.unn.webservice.storage;

import ru.unn.webservice.infrastructure.Statistic;

public class LoadStatDataResponse implements IDataResponse {
    public Statistic statistic;
    public String status;

    public LoadStatDataResponse(Statistic statistic, String status) {
        this.statistic = statistic;
        this.status = status;
    }
}
