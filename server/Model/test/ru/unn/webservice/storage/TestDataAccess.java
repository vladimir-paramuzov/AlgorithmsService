package ru.unn.webservice.storage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;
import static ru.unn.webservice.storage.User.TYPE.*;
import static ru.unn.webservice.storage.Algorithm.Language.*;

public class TestDataAccess {
    public TestDataAccess() {
        tags1.add("tag1");
        tags1.add("tag2");
        tags2.add("tag1");
        tags3.add("tag1");
        tags3.add("tag2");
        tags3.add("tag3");

        purchasedAlgorithms1.add("alg1");
        purchasedAlgorithms2.add("alg1");
        purchasedAlgorithms2.add("alg2");
        purchasedAlgorithms3.add("alg1");
        purchasedAlgorithms3.add("alg2");
        purchasedAlgorithms3.add("alg3");

        downloads.add(Calendar.getInstance().getTime());
        purchases.add(Calendar.getInstance().getTime());
    }

    @Before
    public void setUp() {
        dataAccess = new DataAccess();

        dataAccess.deleteUsers();
        dataAccess.deleteAlgorithms();
        dataAccess.deleteStatistic();

        Algorithm alg1 = new Algorithm("alg1", "superalg1", tags1, sourceFile, testFile, "testUser", 10, CPP);
        Algorithm alg2 = new Algorithm("alg2", "superalg2", tags2, sourceFile, testFile, "testDeveloper", 20, CPP);
        Algorithm alg3 = new Algorithm("alg3", "superalg3", tags3, sourceFile, testFile, "testAdmin", 30, CPP);

        dataAccess.writeAlgorithm(alg1);
        dataAccess.writeAlgorithm(alg2);
        dataAccess.writeAlgorithm(alg3);

        User user = new User("testUser", "user", USER, 100, purchasedAlgorithms1);
        User developer = new User("testDeveloper", "developer", DEVELOPER, 200, purchasedAlgorithms2);
        User admin = new User("testAdmin", "admin", ADMIN, 300, purchasedAlgorithms3);

        dataAccess.writeUser(user);
        dataAccess.writeUser(developer);
        dataAccess.writeUser(admin);

        Statistic statistic = new Statistic(downloads, purchases);
        dataAccess.writeStatistic(statistic);
    }

    @Test
    public void canGetAlgorithm() {
        IDataRequest request = new LoadAlgorithmDataRequest("alg1");

        Algorithm algorithm = ((LoadAlgorithmDataResponse)dataAccess.process(request)).algorithm;

        assertEquals("alg1", algorithm.name);
        assertEquals("superalg1", algorithm.description);
        assertEquals(tags1, algorithm.tags);
        assertArrayEquals(sourceFile, algorithm.sourceFile);
        assertArrayEquals(testFile, algorithm.testFile);
        assertEquals("testUser", algorithm.owner);
        assertEquals(10, algorithm.cost);
        assertEquals(CPP, algorithm.lang);

    }

    @Test
    public void canAddAlgorithm() {
        Algorithm algorithm = new Algorithm("newalg", "superalg1", tags1, sourceFile, testFile, "testUser", 10, CPP);
        IDataRequest request = new StoreAlgorithmDataRequest(algorithm);

        String status = ((StoreAlgorithmDataResponse)dataAccess.process(request)).status;

        assertEquals("OK", status);
    }

    @Test
    public void cantAddAlgorithmWithNonUniqueName() {
        Algorithm algorithm = new Algorithm("alg1", "superalg", tags2, sourceFile, testFile, "testUser", 10, CPP);
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
        User user = new User("testUser1", "user1", User.TYPE.USER, 101, purchasedAlgorithms1);
        IDataRequest request = new StoreUserDataRequest(user);

        String status = ((StoreUserDataResponse)dataAccess.process(request)).status;

        assertEquals("OK", status);
    }

    @Test
    public void cantAddUserWithNonUniqueLogin() {
        User user = new User("testUser", "user1", User.TYPE.USER, 102, purchasedAlgorithms1);
        IDataRequest request = new StoreUserDataRequest(user);

        String status = ((StoreUserDataResponse)dataAccess.process(request)).status;

        assertNotEquals("OK", status);
    }

    @Test
    public void canGetStatistic() {
        IDataRequest request = new LoadStatDataRequest();

        Statistic statistic = ((LoadStatDataResponse)dataAccess.process(request)).statistic;

        assertEquals(1, statistic.getDownloadsCount(null, null));
        assertEquals(1, statistic.getPurchasesCount(null, null));
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
        dataAccess.deleteUsers();
        dataAccess.deleteAlgorithms();
        dataAccess.deleteStatistic();
    }

    private DataAccess dataAccess;
    private String code = "#include <cstdio>";
    private String testdata = "Here will be test data";
    private byte[] sourceFile = code.getBytes(Charset.forName("UTF-8"));
    private byte[] testFile = testdata.getBytes(Charset.forName("UTF-8"));
    private ArrayList<String> tags1 = new ArrayList<>();
    private ArrayList<String> tags2 = new ArrayList<>();
    private ArrayList<String> tags3 = new ArrayList<>();
    private ArrayList<String> purchasedAlgorithms1 = new ArrayList<>();
    private ArrayList<String> purchasedAlgorithms2 = new ArrayList<>();
    private ArrayList<String> purchasedAlgorithms3 = new ArrayList<>();
    private ArrayList<Date> downloads = new ArrayList<>();
    private ArrayList<Date> purchases = new ArrayList<>();
}
