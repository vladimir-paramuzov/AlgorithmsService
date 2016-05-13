package ru.unn.webservice.infrastructure;

import java.util.ArrayList;

public class SearchAlgorithmResponse implements IResponse {
    public ArrayList<Algorithm> algorithmsList;
    public String status;

    public SearchAlgorithmResponse(ArrayList<Algorithm> algorithmsList, String status) {
        this.algorithmsList = algorithmsList;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<Algorithm> getAlgorithmsList() {
        return algorithmsList;
    }
}
