package ru.unn.webservice.test_automation;

import ru.unn.webservice.infrastructure.Language;

public class CPPTestSystem implements ITestSystem {
    public CPPTestSystem() {
        cppTester = new WindowsGCC_CPP_Tester();
    }

    @Override
    public Language getLanguage() {
        return lang;
    }

    @Override
    public byte[] test(String executablePath, String testDataPath) {
        return cppTester.test(executablePath, testDataPath);
    }

    private Language lang = Language.CPP;
    ICPPTester cppTester;
}
