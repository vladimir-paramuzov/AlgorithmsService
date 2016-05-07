package ru.unn.webservice.test_automation;

import ru.unn.webservice.server.TestAlgorithmRequest;
import ru.unn.webservice.server.TestAlgorithmResponse;
import ru.unn.webservice.storage.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class BuildBot implements IBuildBot {
    public BuildBot(IDataAccess dataAccess) {
        this.dataAccess = dataAccess;

        IBuildSystem cppBuilder = new CPPBuildSystem();
        ITestSystem cppTester = new CPPTestSystem();

        buildSystems.add(cppBuilder);
        testSystems.add(cppTester);
    }

    @Override
    public TestAlgorithmResponse process(TestAlgorithmRequest request) {
        try {
            LoadAlgorithmDataResponse response = (LoadAlgorithmDataResponse)dataAccess.process(
                    new LoadAlgorithmDataRequest(request.algorithmName));

            if (!response.status.equals("OK")) {
                return new TestAlgorithmResponse(null, response.status);
            }

            IBuildSystem builder = null;
            for (IBuildSystem buildSystem : buildSystems) {
                if (response.algorithm.lang.equals(buildSystem.getLanguage())) {
                    builder = buildSystem;
                    break;
                }
            }

            if (builder == null) {
                return new TestAlgorithmResponse(null, response.algorithm.lang + " BUILD SYSTEM NOT EXISTS");
            }

            File buildDir = new File(dataAccess.getAlgorithmsPath() + response.algorithm.name + "/build/");
            if (buildDir.exists()) {
                deleteDirectory(buildDir);
            }

            buildDir.mkdir();
            String sourceFilePath = dataAccess.getAlgorithmsPath() + response.algorithm.name + "/main.cpp";
            String outFilePath = dataAccess.getAlgorithmsPath() + response.algorithm.name + "/build/prog.a";
            byte[] buildLog = builder.build(sourceFilePath, outFilePath);

            String executablePath = dataAccess.getAlgorithmsPath() + response.algorithm.name + "/build/prog.a";
            File executableFile = new File(executablePath);
            if (!executableFile.exists()) {
                return new TestAlgorithmResponse(buildLog, "BUILD FAILED");
            }


            ITestSystem tester = null;
            for (ITestSystem testSystem : testSystems) {
                if (response.algorithm.lang.equals(testSystem.getLanguage())) {
                    tester = testSystem;
                    break;
                }
            }

            if (tester == null) {
                return new TestAlgorithmResponse(null, response.algorithm.lang + " TEST SYSTEM NOT EXISTS");
            }

            String testDataPath =  dataAccess.getAlgorithmsPath() + response.algorithm.name;
            if (request.userTestData != null) {
                testDataPath += "/build";
                String decodedSource = new String(request.userTestData, "UTF-8");
                BufferedWriter writer = new BufferedWriter(new FileWriter(testDataPath + "/test_data.txt"));
                writer.write(decodedSource);
                writer.flush();
                writer.close();
            }
            testDataPath += "/test_data.txt";

            byte[] testLog = tester.test(executablePath, testDataPath);

            deleteDirectory(buildDir);
            return new TestAlgorithmResponse(testLog, "OK");
        } catch (Exception ex) {
            return new TestAlgorithmResponse(null, "FAIL");
        }

    }

    static boolean deleteDirectory(File directory) {
        if(directory.exists()){
            File[] files = directory.listFiles();
            if(null!=files){
                for(int i=0; i<files.length; i++) {
                    if(files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    }
                    else {
                        files[i].delete();
                    }
                }
            }
        }
        return (directory.delete());
    }

    IDataAccess dataAccess;
}
