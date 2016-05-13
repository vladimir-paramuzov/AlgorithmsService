package ru.unn.webservice.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.unn.webservice.TestDataInitializer;
import ru.unn.webservice.infrastructure.AuthorizationRequest;
import ru.unn.webservice.infrastructure.AuthorizationResponse;
import ru.unn.webservice.storage.DataAccess;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestServer {
    @Before
    public void setUp() {
        dataAccess = new DataAccess();
        server = new Server(true);
        testData = new TestDataInitializer(dataAccess);
        testData.init();
        server.run();
        while (!server.isConnectorAlive());
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void canConnectToServer() throws Exception {
        Socket socket = new Socket(InetAddress.getLocalHost(), 9999);
        socket.close();

        assertNotNull(socket);
    }

    @Test
    public void canCommunicateWithServer() throws Exception {
        Socket socket = new Socket(InetAddress.getLocalHost(), 9999);
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

        AuthorizationRequest request = new AuthorizationRequest("testUser", "user");
        oos.writeObject(request);
        AuthorizationResponse response = (AuthorizationResponse)ois.readObject();

        oos.close();
        ois.close();
        socket.close();

        assertEquals("OK", response.status);
        assertEquals(testData.user, response.user);
    }


    @After
    public void tearDown() {
        testData.clear();
        server.close();
    }

    private DataAccess dataAccess;
    private Server server;
    private TestDataInitializer testData;
}
