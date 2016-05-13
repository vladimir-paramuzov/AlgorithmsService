package ru.unn.webservice.storage;

import java.nio.file.Path;

public interface IDataAccess {
    IDataResponse process(IDataRequest request);
    Path getAlgorithmsPath();
}
