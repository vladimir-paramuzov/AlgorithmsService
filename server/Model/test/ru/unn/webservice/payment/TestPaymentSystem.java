package ru.unn.webservice.payment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.unn.webservice.TestDataInitializer;
import ru.unn.webservice.server.ChangeBalanceRequest;
import ru.unn.webservice.server.ChangeBalanceResponse;
import ru.unn.webservice.storage.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestPaymentSystem {
    @Before
    public void setUp() {
        dataAccess = new DataAccess();
        paymentSystem = new PaymentSystemEmulator(dataAccess);
        testData = new TestDataInitializer((DataAccess)dataAccess);
        testData.init();
    }

    @Test
    public void canAddMoney() {
        int delta = 10;
        ChangeBalanceRequest request = new ChangeBalanceRequest(delta, "testAdmin");

        ChangeBalanceResponse response = (ChangeBalanceResponse)paymentSystem.process(request);

        User user = ((LoadUserDataResponse)dataAccess.process(new LoadUserDataRequest("testAdmin"))).user;
        assertEquals("OK", response.status);
        assertEquals(310, user.balance);
    }

    @Test
    public void canSubMoney() {
        int delta = -10;
        ChangeBalanceRequest request = new ChangeBalanceRequest(delta, "testAdmin");

        ChangeBalanceResponse response = (ChangeBalanceResponse)paymentSystem.process(request);

        User user = ((LoadUserDataResponse)dataAccess.process(new LoadUserDataRequest("testAdmin"))).user;
        assertEquals("OK", response.status);
        assertEquals(290, user.balance);
    }

    @Test
    public void cantSubMuchMoneyThanUserHas() {
        int delta = -310;
        ChangeBalanceRequest request = new ChangeBalanceRequest(delta, "testAdmin");

        ChangeBalanceResponse response = (ChangeBalanceResponse)paymentSystem.process(request);

        User user = ((LoadUserDataResponse)dataAccess.process(new LoadUserDataRequest("testAdmin"))).user;
        assertNotEquals("OK", response.status);
        assertEquals(300, user.balance);
    }

    @After
    public void tearDown() {
        testData.clear();
    }

    private IDataAccess dataAccess;
    private IPaymentSystem paymentSystem;
    private TestDataInitializer testData;
}
