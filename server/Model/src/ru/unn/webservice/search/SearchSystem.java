package ru.unn.webservice.search;

import ru.unn.webservice.server.SearchAlgorithmResponse;
import ru.unn.webservice.server.SearchAlgorithmsRequest;
import ru.unn.webservice.storage.*;

import java.util.ArrayList;

public class SearchSystem implements ISearchSystem {
    IDataAccess dataAccess;

    public SearchSystem(IDataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    @Override
    public SearchAlgorithmResponse searchAlgorithms(SearchAlgorithmsRequest request) {
        LoadAlgorithmsListDataResponse response = (LoadAlgorithmsListDataResponse)dataAccess.process(
                                                new LoadAlgorithmsListDataRequest());
        if (!response.status.equals("OK")) {
            return new SearchAlgorithmResponse(null, "FAIL");
        }

        ArrayList<Algorithm> fullAlgorithmsList = response.algorithmsList;
        ArrayList<Algorithm> algorithmsList = new ArrayList<>();

        for (Algorithm algorithm : fullAlgorithmsList) {
            if (algorithm.tags.containsAll(request.tags)) {
                algorithmsList.add(algorithm);
            }
        }

        return new SearchAlgorithmResponse(algorithmsList, "OK");
    }
}
