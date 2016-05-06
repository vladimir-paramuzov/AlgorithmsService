package ru.unn.webservice.storage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.unn.webservice.TestDataInitializer;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static ru.unn.webservice.storage.Algorithm.Language.*;

public class TestDataAccess {

    @Before
    public void setUp() {
        dataAccess = new DataAccess();

        testData = new TestDataInitializer(dataAccess);
        testData.init();
    }

    @Test
    public void canGetAlgorithm() {
        IDataRequest request = new LoadAlgorithmDataRequest("alg1");

        Algorithm algorithm = ((LoadAlgorithmDataResponse)dataAccess.process(request)).algorithm;

        assertEquals("alg1", algorithm.name);
        assertEquals("superalg1", algorithm.description);
        assertEquals(testData.tags1, algorithm.tags);
        assertArrayEquals(testData.sourceFile, algorithm.sourceFile);
        assertArrayEquals(testData.testFile, algorithm.testFile);
        assertEquals("testUser", algorithm.owner);
        assertEquals(10, algorithm.cost);
        assertEquals(CPP, algorithm.lang);

    }

    @Test
    public void canAddAlgorithm() {
        Algorithm algorithm = new Algorithm("newalg", "superalg1", testData.tags1,
                                             testData.sourceFile, testData.testFile, "testUser", 10, CPP);
        IDataRequest request = new StoreAlgorithmDataRequest(algorithm);

        String status = ((StoreAlgorithmDataResponse)dataAccess.process(request)).status;

        assertEquals("OK", status);
    }

    @Test
    public void cantAddAlgorithmWithNonUniqueName() {
        Algorithm algorithm = new Algorithm("alg1", "superalg", testData.tags2,
                                             testData.sourceFile, testData.testFile, "testUser", 10, CPP);
        IDataRequest request = new StoreAlgorithmDataRequest(algorithm);

        String status = ((StoreAlgorithmDataResponse)dataAccess.process(request)).status;

        assertNotEquals("OK", status);
    }

    @Test
    public void canGetUser() {
        IDataRequest request = new LoadUserDataRequest("testUser");

        User user = ((LoadUserDataResponse)dataAccess.process(request)).user;

        assertEquals("testUser", user.login);
        assertEquals("user", user.pass);
        assertEquals(User.TYPE.USER, user.type);
        assertEquals(100, user.money);
    }

    @Test
    public void canAddUser() {
        User user = new User("testUser1", "user1", User.TYPE.USER, 101, testData.purchasedAlgorithms1);
        IDataRequest request = new StoreUserDataRequest(user);

        String status = ((StoreUserDataResponse)dataAccess.process(request)).status;

        assertEquals("OK", status);
    }

    @Test
    public void cantAddUserWithNonUniqueLogin() {
        User user = new User("testUser", "user1", User.TYPE.USER, 102, testData.purchasedAlgorithms1);
        IDataRequest request = new StoreUserDataRequest(user);

        String status = ((StoreUserDataResponse)dataAccess.process(request)).status;

        assertNotEquals("OK", status);
    }

    @Test
    public void canGetStatistic() {
        IDataRequest request = new LoadStatDataRequest();

        Statistic statistic = ((LoadStatDataResponse)dataAccess.process(request)).statistic;

        assertEquals(1, statistic.getDownloadsCount());
        assertEquals(1, statistic.getPurchasesCount());
    }

    @Test
    public void canGetUsersList() {
        IDataRequest request = new LoadUsersListDataRequest();
        ArrayList<User> usersList = ((LoadUsersListDataResponse)dataAccess.process(request)).usersList;

        assertNotNull(usersList);
        assertEquals(3, usersList.size());
    }

    @Test
    public void canGetAlgorithmsList() {
        IDataRequest request = new LoadAlgorithmsListDataRequest();
        ArrayList<Algorithm> algorithmsList = ((LoadAlgorithmsListDataResponse)dataAccess.process(request)).algorithmsList;

        assertNotNull(algorithmsList);
        assertEquals(3, algorithmsList.size());
    }

    @After
    public void tearDown() {
        testData.clear();
    }

    private DataAccess dataAccess;
    private TestDataInitializer testData;
}
