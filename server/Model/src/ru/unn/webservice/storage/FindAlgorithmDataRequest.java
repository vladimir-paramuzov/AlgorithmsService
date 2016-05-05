package ru.unn.webservice.storage;

import java.util.ArrayList;

public class FindAlgorithmDataRequest implements IDataRequest {
    public ArrayList<String> tags;

    public FindAlgorithmDataRequest(ArrayList<String> tags) {
        this.tags = tags;
    }
}
