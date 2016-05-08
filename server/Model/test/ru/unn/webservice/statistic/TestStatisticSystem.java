package ru.unn.webservice.statistic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.unn.webservice.TestDataInitializer;
import ru.unn.webservice.server.GetStatisticRequest;
import ru.unn.webservice.server.GetStatisticResponse;
import ru.unn.webservice.storage.DataAccess;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

public class TestStatisticSystem {
    @Before
    public void setUp() {
        dataAccess = new DataAccess();
        statisticCollector = new StatisticCollector(dataAccess);
        testData = new TestDataInitializer(dataAccess);
        testData.init();
    }

    @Test
    public void canGetStatistic() {
        GetStatisticRequest request = new GetStatisticRequest(null, null);

        GetStatisticResponse response = (GetStatisticResponse)statisticCollector.process(request);

        assertEquals("OK", response.status);
        assertEquals(1, response.statistic.getDownloadsCount());
        assertEquals(1, response.statistic.getPurchasesCount());
    }

    @Test
    public void canAddDownloadDate() {
        GetStatisticRequest request = new GetStatisticRequest(null, null);

        statisticCollector.addDownloadDate(Calendar.getInstance().getTime());
        GetStatisticResponse response = (GetStatisticResponse)statisticCollector.process(request);

        assertEquals("OK", response.status);
        assertEquals(2, response.statistic.getDownloadsCount());
        assertEquals(1, response.statistic.getPurchasesCount());
    }

    @Test
    public void canAddPurchaseDate() {
        GetStatisticRequest request = new GetStatisticRequest(null, null);

        statisticCollector.addPurchaseDate(Calendar.getInstance().getTime());
        GetStatisticResponse response = (GetStatisticResponse)statisticCollector.process(request);

        assertEquals("OK", response.status);
        assertEquals(1, response.statistic.getDownloadsCount());
        assertEquals(2, response.statistic.getPurchasesCount());
    }

    @After
    public void tearDown() {
        testData.clear();
    }

    private DataAccess dataAccess;
    private StatisticCollector statisticCollector;
    private TestDataInitializer testData;
}