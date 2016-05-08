package ru.unn.webservice.statistic;

import ru.unn.webservice.server.IRequest;
import ru.unn.webservice.server.IResponse;

import java.util.Date;

public interface IStatisticCollector {
    IResponse process(IRequest request);
    String addDownloadDate(Date date);
    String addPurchaseDate(Date date);
}
