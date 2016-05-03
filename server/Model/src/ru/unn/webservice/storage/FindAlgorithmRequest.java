package ru.unn.webservice.storage;

import java.util.ArrayList;

public class FindAlgorithmRequest implements IDataRequest {
    public ArrayList<String> tags;

    public FindAlgorithmRequest(ArrayList<String> tags) {
        this.tags = tags;
    }
}
