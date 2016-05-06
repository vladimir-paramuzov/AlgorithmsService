package ru.unn.webservice.statistic;

import ru.unn.webservice.server.GetStatisticRequest;
import ru.unn.webservice.server.GetStatisticResponse;

import java.util.Date;

public interface IStatisticCollector {
    GetStatisticResponse getStatistic(GetStatisticRequest request);
    String addDownloadDate(Date date);
    String addPurchaseDate(Date date);
}
