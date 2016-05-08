package ru.unn.webservice.storage;

import ru.unn.webservice.infrastructure.Algorithm;

import java.util.ArrayList;

public class LoadAlgorithmsListDataResponse implements IDataResponse {
    public ArrayList<Algorithm> algorithmsList;
    public String status;

    public LoadAlgorithmsListDataResponse(ArrayList<Algorithm> algorithmsList, String status) {
        this.algorithmsList = algorithmsList;
        this.status = status;
    }
}
