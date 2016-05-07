package ru.unn.webservice.storage;

public interface IDataAccess {
    IDataResponse process(IDataRequest request);
    String getAlgorithmsPath();
}
