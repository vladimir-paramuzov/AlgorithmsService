package ru.unn.webservice.server;

import java.util.ArrayList;

public class SearchAlgorithmsRequest implements IRequest {
    public ArrayList<String> tags;

    public SearchAlgorithmsRequest(ArrayList<String> tags) {
        this.tags = tags;
    }
}
