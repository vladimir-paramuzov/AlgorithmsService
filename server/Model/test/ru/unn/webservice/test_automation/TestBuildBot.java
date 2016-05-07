package ru.unn.webservice.test_automation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.unn.webservice.TestDataInitializer;
import ru.unn.webservice.server.GetStatisticResponse;
import ru.unn.webservice.server.TestAlgorithmRequest;
import ru.unn.webservice.server.TestAlgorithmResponse;
import ru.unn.webservice.storage.DataAccess;

import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;

public class TestBuildBot {
    @Before
    public void setUp() {
        dataAccess = new DataAccess();
        buildbot = new BuildBot(dataAccess);
        testData = new TestDataInitializer(dataAccess);
        testData.init();
    }

    @Test
    public void canTestAlgorithm() throws Exception {
        TestAlgorithmRequest request = new TestAlgorithmRequest("alg1", null);

        TestAlgorithmResponse response = buildbot.process(request);
        String decodedLog = new String(response.log, "UTF-8");

        assertEquals("OK", response.status);
        assertEquals(testData.testdata, decodedLog);
    }

    @Test
    public void canTestAlgorithmWithUserData() throws Exception {
        String userData = "Here is user string";

        TestAlgorithmRequest request = new TestAlgorithmRequest("alg1", userData.getBytes(Charset.forName("UTF-8")));

        TestAlgorithmResponse response = buildbot.process(request);
        String decodedLog = new String(response.log, "UTF-8");

        assertEquals("OK", response.status);
        assertEquals(userData, decodedLog);
    }

    @After
    public void tearDown() {
        testData.clear();
    }

    private DataAccess dataAccess;
    private IBuildBot buildbot;
    private TestDataInitializer testData;
}
