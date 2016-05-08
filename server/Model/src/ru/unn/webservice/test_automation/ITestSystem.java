package ru.unn.webservice.test_automation;

import ru.unn.webservice.infrastructure.Language;

public interface ITestSystem {
    Language getLanguage();
    byte[] test(String executablePath, String testDataPath);
}
