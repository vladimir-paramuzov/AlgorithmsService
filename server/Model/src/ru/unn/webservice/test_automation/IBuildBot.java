package ru.unn.webservice.test_automation;

import ru.unn.webservice.server.IRequest;
import ru.unn.webservice.server.IResponse;

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

    IResponse process(IRequest request);
    STATUS getStatus();
}
