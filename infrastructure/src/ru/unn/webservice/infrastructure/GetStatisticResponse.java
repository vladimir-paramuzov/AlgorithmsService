package ru.unn.webservice.infrastructure;

public class GetStatisticResponse implements IResponse {
    public Statistic statistic;
    public String status;

    public GetStatisticResponse(Statistic statistic, String status) {
        this.statistic = statistic;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public Statistic getStatistic() {
        return statistic;
    }
}
