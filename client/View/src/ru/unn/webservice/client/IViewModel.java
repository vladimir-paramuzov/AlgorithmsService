package ru.unn.webservice.client;

import java.util.List;

public interface IViewModel {
    boolean isSearchButtonEnabled();
    void setTag(final String tag);

    String getTag();
    List getListAlgorithms();

    void searchAlgorithm();
}
