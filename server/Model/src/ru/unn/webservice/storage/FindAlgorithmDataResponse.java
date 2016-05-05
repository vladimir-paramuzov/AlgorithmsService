package ru.unn.webservice.storage;

import java.util.ArrayList;

public class FindAlgorithmDataResponse implements IDataResponse {
    public String status;
    public ArrayList<Algorithm> algorithms;

    public FindAlgorithmDataResponse(ArrayList<Algorithm> algorithms, String status) {
        this.status = status;
        this.algorithms = algorithms;
    }
}
