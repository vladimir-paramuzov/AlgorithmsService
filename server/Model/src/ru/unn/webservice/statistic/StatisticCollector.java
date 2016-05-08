package ru.unn.webservice.statistic;

import ru.unn.webservice.infrastructure.*;
import ru.unn.webservice.storage.*;

import java.util.ArrayList;
import java.util.Date;

public class StatisticCollector implements IStatisticCollector {
    public StatisticCollector(IDataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    @Override
    public IResponse process(IRequest request) {
        if (request instanceof GetStatisticRequest) {
            return getStatistic((GetStatisticRequest)request);
        } else {
            return null;
        }
    }

    private GetStatisticResponse getStatistic(GetStatisticRequest request) {
        LoadStatDataResponse response = (LoadStatDataResponse)dataAccess.process(
                new LoadStatDataRequest());
        if (!response.status.equals("OK")) {
            return new GetStatisticResponse(null, "FAIL");
        }

        ArrayList<Date> downloads = response.statistic.getDownloadsList(request.from, request.to);
        ArrayList<Date> purchases = response.statistic.getPurchasesList(request.from, request.to);

        Statistic statistic = new Statistic(downloads, purchases);

        return new GetStatisticResponse(statistic, "OK");
    }

    @Override
    public String addDownloadDate(Date date) {
        LoadStatDataResponse response = (LoadStatDataResponse)dataAccess.process(
                new LoadStatDataRequest());
        if (!response.status.equals("OK")) {
            return response.status;
        }

        Statistic statistic = response.statistic;
        statistic.addDownload(date);

        StoreStatDataResponse response1 = (StoreStatDataResponse)dataAccess.process(
                                            new StoreStatDataRequest(statistic));
        if (!response1.status.equals("OK")) {
            return response1.status;
        }

        return "OK";
    }

    @Override
    public String addPurchaseDate(Date date) {
        LoadStatDataResponse response = (LoadStatDataResponse)dataAccess.process(
                new LoadStatDataRequest());
        if (!response.status.equals("OK")) {
            return response.status;
        }

        Statistic statistic = response.statistic;
        statistic.addPurchase(date);

        StoreStatDataResponse response1 = (StoreStatDataResponse)dataAccess.process(
                new StoreStatDataRequest(statistic));
        if (!response1.status.equals("OK")) {
            return response1.status;
        }

        return "OK";
    }

    private IDataAccess dataAccess;
}
