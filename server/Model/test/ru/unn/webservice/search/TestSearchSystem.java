package ru.unn.webservice.search;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.unn.webservice.TestDataInitializer;
import ru.unn.webservice.server.SearchAlgorithmResponse;
import ru.unn.webservice.server.SearchAlgorithmsRequest;
import ru.unn.webservice.storage.DataAccess;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TestSearchSystem {
    @Before
    public void setUp() {
        dataAccess = new DataAccess();
        searchSystem = new SearchSystem(dataAccess);
        testData = new TestDataInitializer(dataAccess);
        testData.init();
    }

    @Test
    public void canSearchAlgorithms() {
        ArrayList<String> tags = new ArrayList<>();
        tags.add("tag1");
        tags.add("tag2");
        SearchAlgorithmsRequest request = new SearchAlgorithmsRequest(tags);

        SearchAlgorithmResponse response = searchSystem.searchAlgorithms(request);

        assertEquals("OK", response.status);
        assertEquals(2, response.algorithmsList.size());
    }

    @After
    public void tearDown() {
        testData.clear();
    }

    private DataAccess dataAccess;
    private SearchSystem searchSystem;
    private TestDataInitializer testData;
}
