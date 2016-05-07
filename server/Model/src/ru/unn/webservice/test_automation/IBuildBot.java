package ru.unn.webservice.test_automation;

import ru.unn.webservice.server.TestAlgorithmRequest;
import ru.unn.webservice.server.TestAlgorithmResponse;

import java.util.ArrayList;

public interface IBuildBot {
    enum STATUS {
        FREE("FREE"),
        BUSY("BUSY");

        String status;

        STATUS(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return status;
        }
    }
    ArrayList<IBuildSystem> buildSystems = new ArrayList<>();
    ArrayList<ITestSystem> testSystems   = new ArrayList<>();

    TestAlgorithmResponse process(TestAlgorithmRequest request);
}
