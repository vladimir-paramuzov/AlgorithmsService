package ru.unn.webservice.test_automation;

import ru.unn.webservice.storage.Language;

public interface ITestSystem {
    Language getLanguage();
    byte[] test(String executablePath, String testDataPath);
}
