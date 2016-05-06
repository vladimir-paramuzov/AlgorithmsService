package ru.unn.webservice.server;

import ru.unn.webservice.storage.Statistic;

public class GetStatisticResponse implements IResponse {
    public Statistic statistic;
    public String status;

    public GetStatisticResponse(Statistic statistic, String status) {
        this.statistic = statistic;
        this.status = status;
    }
}
