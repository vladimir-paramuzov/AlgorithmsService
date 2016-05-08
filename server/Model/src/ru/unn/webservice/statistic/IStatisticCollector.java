package ru.unn.webservice.statistic;

import ru.unn.webservice.infrastructure.IRequest;
import ru.unn.webservice.infrastructure.IResponse;

import java.util.Date;

public interface IStatisticCollector {
    IResponse process(IRequest request);
    String addDownloadDate(Date date);
    String addPurchaseDate(Date date);
}
