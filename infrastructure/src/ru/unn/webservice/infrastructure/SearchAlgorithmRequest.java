package ru.unn.webservice.infrastructure;

import java.util.ArrayList;

public class SearchAlgorithmRequest implements IRequest {
    public ArrayList<String> tags;

    public SearchAlgorithmRequest(ArrayList<String> tags) {
        this.tags = tags;
    }
}
