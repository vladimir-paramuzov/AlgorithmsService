package ru.unn.webservice.infrastructure;

public class ChangeBalanceResponse implements IResponse {
    public String status;

    public ChangeBalanceResponse(String status) {
        this.status = status;
    }
}
