package ru.unn.webservice.search;

import ru.unn.webservice.server.SearchAlgorithmResponse;
import ru.unn.webservice.server.SearchAlgorithmsRequest;

public interface ISearchSystem {
    SearchAlgorithmResponse searchAlgorithms(SearchAlgorithmsRequest request);
}
