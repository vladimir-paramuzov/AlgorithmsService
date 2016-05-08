package ru.unn.webservice.test_automation;

import ru.unn.webservice.infrastructure.Language;

public class CPPBuildSystem implements IBuildSystem {
    public CPPBuildSystem() {
        cppBuilder = new WindowsGCC_CPP_Builder();
    }

    @Override
    public Language getLanguage() {
        return lang;
    }

    @Override
    public byte[] build(String sourceFilePath, String outputFilePath) {
        return cppBuilder.build(sourceFilePath, outputFilePath);
    }

    private Language lang = Language.CPP;
    ICPPBuilder cppBuilder;
}
