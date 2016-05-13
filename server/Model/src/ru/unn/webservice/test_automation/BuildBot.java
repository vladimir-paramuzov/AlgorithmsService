package ru.unn.webservice.test_automation;

import ru.unn.webservice.infrastructure.IRequest;
import ru.unn.webservice.infrastructure.IResponse;
import ru.unn.webservice.infrastructure.TestAlgorithmRequest;
import ru.unn.webservice.infrastructure.TestAlgorithmResponse;
import ru.unn.webservice.storage.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;

import static ru.unn.webservice.test_automation.IBuildBot.STATUS.*;

public class BuildBot implements IBuildBot {
    public BuildBot(IDataAccess dataAccess) {
        status = FREE;
        this.dataAccess = dataAccess;

        IBuildSystem cppBuilder = new CPPBuildSystem();
        ITestSystem cppTester = new CPPTestSystem();

        buildSystems.add(cppBuilder);
        testSystems.add(cppTester);
    }

    @Override
    public IResponse process(IRequest request) {
        if (request instanceof TestAlgorithmRequest) {
            return testAlgorithm((TestAlgorithmRequest)request);
        } else {
            return null;
        }
    }

    private TestAlgorithmResponse testAlgorithm(TestAlgorithmRequest request) {
        try {
            status = BUSY;
            LoadAlgorithmDataResponse response = (LoadAlgorithmDataResponse)dataAccess.process(
                                                  new LoadAlgorithmDataRequest(request.algorithmName));

            if (!response.status.equals("OK")) {
                status = FREE;
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
                status = FREE;
                return new TestAlgorithmResponse(null, response.algorithm.lang + " BUILD SYSTEM NOT EXISTS");
            }

            File buildDir = new File(dataAccess.getAlgorithmsPath().resolve(response.algorithm.name).resolve("build").toString());
            if (buildDir.exists()) {
                deleteDirectory(buildDir);
            }

            buildDir.mkdir();
            String sourceFilePath = dataAccess.getAlgorithmsPath().resolve(response.algorithm.name).resolve("main.cpp").toString();
            String outFilePath = dataAccess.getAlgorithmsPath().resolve(response.algorithm.name).resolve("build").resolve("prog.a").toString();
            byte[] buildLog = builder.build(sourceFilePath, outFilePath);

            String executablePath = outFilePath;
            File executableFile = new File(executablePath);
            if (!executableFile.exists()) {
                status = FREE;
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
                status = FREE;
                return new TestAlgorithmResponse(null, response.algorithm.lang + " TEST SYSTEM NOT EXISTS");
            }

            Path testDataPath =  dataAccess.getAlgorithmsPath().resolve(response.algorithm.name);
            if (request.userTestData != null) {
                testDataPath = testDataPath.resolve("build").resolve("test_data.txt");
                String decodedSource = new String(request.userTestData, "UTF-8");
                BufferedWriter writer = new BufferedWriter(new FileWriter(testDataPath.toString()));
                writer.write(decodedSource);
                writer.flush();
                writer.close();
            } else {
                testDataPath = testDataPath.resolve("test_data.txt");
            }

            byte[] testLog = tester.test(executablePath, testDataPath.toString());

            deleteDirectory(buildDir);
            status = FREE;
            return new TestAlgorithmResponse(testLog, "OK");
        } catch (Exception ex) {
            status = FREE;
            return new TestAlgorithmResponse(null, "FAIL");
        }
    }

    static boolean deleteDirectory(File directory) {
        if(directory.exists()){
            File[] files = directory.listFiles();
            if(null != files){
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }
        return (directory.delete());
    }

    @Override
    public STATUS getStatus() {
        return status;
    }

    private STATUS status;
    private IDataAccess dataAccess;
}
