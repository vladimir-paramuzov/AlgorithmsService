package ru.unn.webservice.storage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.ArrayList;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
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

        User user = new User("testUser", "user", USER, 100);
        User developer = new User("testDeveloper", "developer", DEVELOPER, 200);
        User admin = new User("testAdmin", "admin", ADMIN, 300);

        dataAccess.writeUser(user);
        dataAccess.writeUser(developer);
        dataAccess.writeUser(admin);

    }

    @Test
    public void canGetAlgorithm() throws Exception {
        IDataRequest request = new GetAlgorithmDataRequest("alg1");

        Algorithm algorithm = ((GetAlgorithmDataResponse)dataAccess.process(request)).algorithm;

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
    public void canGetUser() {
        IDataRequest request = new GetUserDataRequest("testUser");

        User user = ((GetUserDataResponse)dataAccess.process(request)).user;

        assertEquals("testUser", user.login);
        assertEquals("user", user.pass);
        assertEquals(User.TYPE.USER, user.type);
        assertEquals(100, user.money);
    }

    @Test
    public void canAddUser() {
        User user = new User("testUser1", "user1", User.TYPE.USER, 101);
        IDataRequest request = new AddUserDataRequest(user);

        String status = ((AddUserDataResponse)dataAccess.process(request)).status;

        assertEquals("OK", status);
    }

    @Test
    public void cantAddUserWithNonUniqueLogin() {
        User user = new User("testUser", "user1", User.TYPE.USER, 102);
        IDataRequest request = new AddUserDataRequest(user);

        String status = ((AddUserDataResponse)dataAccess.process(request)).status;

        assertNotEquals("OK", status);
    }

    @Test
    public void canGetStatistic() {
        IDataRequest request = new GetStatDataRequest(null, null);

        GetStatDataResponse response = (GetStatDataResponse)dataAccess.process(request);

        assertEquals(1, response.downloads);
        assertEquals(0, response.purchases);
    }

    @After
    public void tearDown() {

    }

    private DataAccess dataAccess;
    private String code = "#include <cstdio>";
    private String testdata = "Here will be test data";
    private byte[] sourceFile = code.getBytes(Charset.forName("UTF-8"));
    private byte[] testFile = testdata.getBytes(Charset.forName("UTF-8"));
    private ArrayList<String> tags1 = new ArrayList<>();
    private ArrayList<String> tags2 = new ArrayList<>();
    private ArrayList<String> tags3 = new ArrayList<>();
}
