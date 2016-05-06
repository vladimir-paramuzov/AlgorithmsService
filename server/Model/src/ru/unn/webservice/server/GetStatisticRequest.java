package ru.unn.webservice.server;

import java.util.Date;

public class GetStatisticRequest implements IRequest {
    public Date from;
    public Date to;

    public GetStatisticRequest(Date from, Date to) {
        this.from = from;
        this.to = to;
    }
}
