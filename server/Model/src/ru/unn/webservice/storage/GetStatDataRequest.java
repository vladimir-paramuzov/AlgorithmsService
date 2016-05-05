package ru.unn.webservice.storage;

import java.util.Date;

public class GetStatDataRequest implements IDataRequest {
    public Date from;
    public Date to;

    public GetStatDataRequest(Date from, Date to) {
        this.from = from;
        this.to = to;
    }

    public GetStatDataRequest() {
        from = null;
        to = null;
    }
}
