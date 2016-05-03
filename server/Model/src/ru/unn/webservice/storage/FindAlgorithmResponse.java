package ru.unn.webservice.storage;

import java.util.ArrayList;

public class FindAlgorithmResponse implements IDataResponse {
    public String status;
    public ArrayList<Algorithm> algorithms;

    public FindAlgorithmResponse(ArrayList<Algorithm> algorithms, String status) {
        this.status = status;
        this.algorithms = algorithms;
    }
}
