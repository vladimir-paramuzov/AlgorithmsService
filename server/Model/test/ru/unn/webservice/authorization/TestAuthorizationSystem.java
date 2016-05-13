package ru.unn.webservice.authorization;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.unn.webservice.TestDataInitializer;
import ru.unn.webservice.infrastructure.AuthorizationRequest;
import ru.unn.webservice.infrastructure.AuthorizationResponse;
import ru.unn.webservice.infrastructure.RegisterRequest;
import ru.unn.webservice.infrastructure.RegisterResponse;
import ru.unn.webservice.storage.DataAccess;
import ru.unn.webservice.storage.IDataAccess;
import ru.unn.webservice.infrastructure.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestAuthorizationSystem {
    @Before
    public void setUp() {
        dataAccess = new DataAccess(TestDataInitializer.testDB_Path);
        authorizationSystem = new AuthorizationSystem(dataAccess);
        testData = new TestDataInitializer((DataAccess)dataAccess);
        testData.init();
    }

    @Test
    public void canAuthorizeUserWithCorrectData() {
        AuthorizationRequest request = new AuthorizationRequest("testUser", "user");

        AuthorizationResponse response = (AuthorizationResponse)authorizationSystem.process(request);

        assertEquals("OK", response.status);
        assertEquals(testData.user, response.user);
    }

    @Test
    public void cantAuthorizeUserWithWrongPassword() {
        AuthorizationRequest request = new AuthorizationRequest("testUser", "user1");

        AuthorizationResponse response = (AuthorizationResponse)authorizationSystem.process(request);

        assertEquals("WRONG PASS", response.status);
        assertNull(response.user);
    }

    @Test
    public void cantAuthorizeUserWithWrongLogin() {
        AuthorizationRequest request = new AuthorizationRequest("testUser1", "user");

        AuthorizationResponse response = (AuthorizationResponse)authorizationSystem.process(request);

        assertEquals("WRONG LOGIN", response.status);
        assertNull(response.user);
    }

    @Test
    public void canRegisterUniqueUser() {
        RegisterRequest request = new RegisterRequest("testUser1", "user", User.TYPE.USER);

        RegisterResponse response = (RegisterResponse)authorizationSystem.process(request);

        assertEquals("OK", response.status);
    }

    @Test
    public void cantRegisterNonUniqueUser() {
        RegisterRequest request = new RegisterRequest("testUser", "user", User.TYPE.USER);

        RegisterResponse response = (RegisterResponse)authorizationSystem.process(request);

        assertEquals("USER ALREADY EXISTS", response.status);
    }

    @After
    public void tearDown() {
        testData.clear();
    }

    private IDataAccess dataAccess;
    private IAuthorizationSystem authorizationSystem;
    private TestDataInitializer testData;
}
