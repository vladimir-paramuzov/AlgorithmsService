package ru.unn.webservice.server;

import ru.unn.webservice.storage.Algorithm;

import java.util.ArrayList;

public class SearchAlgorithmResponse implements IResponse {
    public ArrayList<Algorithm> algorithmsList;
    public String status;

    public SearchAlgorithmResponse(ArrayList<Algorithm> algorithmsList, String status) {
        this.algorithmsList = algorithmsList;
        this.status = status;
    }
}
