package ru.unn.webservice.test_automation;

import ru.unn.webservice.storage.Language;

public interface IBuildSystem {
    Language getLanguage();
    byte[] build(String sourceFilePath, String outputFilePath);
}
