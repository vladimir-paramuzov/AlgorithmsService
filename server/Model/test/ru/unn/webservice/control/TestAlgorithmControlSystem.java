package ru.unn.webservice.control;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.unn.webservice.TestDataInitializer;
import ru.unn.webservice.infrastructure.*;
import ru.unn.webservice.payment.IPaymentSystem;
import ru.unn.webservice.payment.PaymentSystemEmulator;
import ru.unn.webservice.statistic.IStatisticCollector;
import ru.unn.webservice.statistic.StatisticCollector;
import ru.unn.webservice.storage.*;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

public class TestAlgorithmControlSystem {
    @Before
    public void setUp() {
        dataAccess = new DataAccess();
        statisticCollector = new StatisticCollector(dataAccess);
        paymentSystem = new PaymentSystemEmulator(dataAccess);
        algorithmControlSystem = new AlgorithmControlSystem(dataAccess, statisticCollector, paymentSystem);
        testData = new TestDataInitializer((DataAccess)dataAccess);
        testData.init();
    }

    @Test
    public void canDownloadAlgorithm() {
        DownloadAlgorithmResponse response = (DownloadAlgorithmResponse)algorithmControlSystem.process(
                                                      new DownloadAlgorithmRequest("testUser", "alg1"));

        assertEquals("OK", response.status);
        assertEquals(testData.alg1, response.algorithm);
    }

    @Test
    public void cantDownloadNotBoughtAlgorithm() {
        DownloadAlgorithmResponse response = (DownloadAlgorithmResponse)algorithmControlSystem.process(
                                                      new DownloadAlgorithmRequest("testUser", "alg2"));

        assertEquals("ALGORITHM IS NOT BOUGHT", response.status);
        assertNull(response.algorithm);
    }

    @Test
    public void canBuyAlgorithm() {
        ArrayList<String> purchasedAlgorithms = testData.user.purchasedAlgorithms;
        purchasedAlgorithms.add("alg2");

        BuyAlgorithmResponse response = (BuyAlgorithmResponse)algorithmControlSystem.process(
                new BuyAlgorithmRequest("testUser", "alg2"));
        LoadUserDataResponse userResponse = (LoadUserDataResponse)dataAccess.process(
                new LoadUserDataRequest("testUser"));

        assertEquals("OK", response.status);
        assertEquals(testData.user.balance - testData.alg2.cost, userResponse.user.balance);
        assertEquals(purchasedAlgorithms, userResponse.user.purchasedAlgorithms);
    }

    @Test
    public void cantBuyAlgorithmWhenNotEnoughMoney() {
        BuyAlgorithmResponse response = (BuyAlgorithmResponse)algorithmControlSystem.process(
                new BuyAlgorithmRequest("testUser", "alg3"));
        LoadUserDataResponse userResponse = (LoadUserDataResponse)dataAccess.process(
                new LoadUserDataRequest("testUser"));

        assertEquals("NOT ENOUGH MONEY", response.status);
        assertEquals(testData.user.balance, userResponse.user.balance);
        assertEquals(testData.user.purchasedAlgorithms, userResponse.user.purchasedAlgorithms);
    }

    @Test
    public void cantBuyAlgorithmWhenItAlreadyBought() {
        BuyAlgorithmResponse response = (BuyAlgorithmResponse)algorithmControlSystem.process(
                new BuyAlgorithmRequest("testUser", "alg1"));
        LoadUserDataResponse userResponse = (LoadUserDataResponse)dataAccess.process(
                new LoadUserDataRequest("testUser"));

        assertEquals("ALGORITHM ALREADY BOUGHT", response.status);
        assertEquals(testData.user.balance, userResponse.user.balance);
        assertEquals(testData.user.purchasedAlgorithms, userResponse.user.purchasedAlgorithms);
    }

    @Test
    public void canAddAlgorithm() {
        Algorithm algorithm = testData.alg1;
        algorithm.name = "alg4";

        AddAlgorithmResponse addAlgorithmResponse = (AddAlgorithmResponse)algorithmControlSystem.process(
                                            new AddAlgorithmRequest(algorithm));

        LoadAlgorithmDataResponse algorithmResponse = (LoadAlgorithmDataResponse)dataAccess.process(
                new LoadAlgorithmDataRequest(algorithm.name));

        assertEquals("OK", addAlgorithmResponse.status);
        assertEquals("OK", algorithmResponse.status);
    }

    @Test
    public void cantAddExistingAlgorithm() {
        AddAlgorithmResponse addAlgorithmResponse = (AddAlgorithmResponse)algorithmControlSystem.process(
                                            new AddAlgorithmRequest(testData.alg1));

        assertEquals("ALGORITHM ALREADY EXISTS", addAlgorithmResponse.status);
    }

    @Test
    public void canSearchAlgorithms() {
        ArrayList<String> tags = new ArrayList<>();
        tags.add("tag1");
        tags.add("tag2");
        SearchAlgorithmRequest request = new SearchAlgorithmRequest(tags);

        SearchAlgorithmResponse response = (SearchAlgorithmResponse)algorithmControlSystem.process(request);

        assertEquals("OK", response.status);
        assertEquals(2, response.algorithmsList.size());
    }
    
    @After
    public void tearDown() {
        testData.clear();
    }

    private IDataAccess dataAccess;
    private IStatisticCollector statisticCollector;
    private IAlgorithmControlSystem algorithmControlSystem;
    private IPaymentSystem paymentSystem;
    private TestDataInitializer testData;
}

