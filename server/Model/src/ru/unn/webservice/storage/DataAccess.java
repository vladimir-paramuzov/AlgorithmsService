package ru.unn.webservice.storage;

import java.io.*;
import java.util.ArrayList;

public class DataAccess implements IDataAccess {

    private static String DB_PATH = "/home/vvp/distrib/dev/AlgorithmsService_Build/";
    private static String USERS_PATH = DB_PATH + "users/";
    private static String ALGORITHMS_PATH = DB_PATH + "algorithms/";

    public IDataResponse process(IDataRequest request) {
        if (request instanceof AddUserRequest) {
            return (IDataResponse)process((AddUserRequest) request);
        } else if (request instanceof GetUserRequest){
            return (IDataResponse)process((GetUserRequest) request);
        } else if (request instanceof AddAlgorithmRequest){
            return (IDataResponse)process((AddAlgorithmRequest)request);
        } else if (request instanceof GetAlgorithmRequest) {
            return (IDataResponse)process((GetAlgorithmRequest)request);
        } else if (request instanceof FindAlgorithmRequest) {
            return (IDataResponse)process((FindAlgorithmRequest)request);
        } else {
            return null;
        }
    }

    private AddUserResponse process(AddUserRequest request) {
        AddUserResponse response = new AddUserResponse();
        File path = new File(USERS_PATH + request.user.login);
        if (!path.exists()) {
            if (path.mkdir()) {
                OutputStream outputStream = null;
                try {
                    byte[] data = getBytes(request.user);
                    outputStream = new FileOutputStream (USERS_PATH + request.user.login + "/data.bin");
                    outputStream.write(data, 0, data.length);
                    outputStream.flush();
                    outputStream.close();
                    response.status = "OK";
                } catch(IOException ex) {
                    response.status = "FAIL";
                } finally {
                    try {
                        outputStream.close();
                    } catch(IOException ex){
                        //
                    }
                }

            } else {
                response.status = "FAIL";
            }
        } else {
            response.status = "USER ALREADY EXISTS";
        }
        return response;
    }

    private GetUserResponse process(GetUserRequest request) {
        FileInputStream fstream = null;
        User user = null;
        try {
            fstream = new FileInputStream(USERS_PATH + request.user.login + "/data.bin");
            ObjectInputStream ostream = new ObjectInputStream(fstream);
            user = (User)ostream.readObject();
            return new GetUserResponse(user, "OK");
        } catch(Exception ex) {
            return new GetUserResponse(request.user, "Fail. Object not found");
        } finally {
            try {
                fstream.close();
            } catch (Exception ex) {

            }
        }
    }

    private AddAlgorithmResponse process(AddAlgorithmRequest request) {
        AddAlgorithmResponse response = new AddAlgorithmResponse();
        File path = new File(ALGORITHMS_PATH + request.algorithm.name);
        if (!path.exists()) {
            if (path.mkdir()) {
                OutputStream outputStream = null;
                BufferedWriter writer = null;
                try {
                    byte[] data = getBytes(request.algorithm);
                    outputStream = new FileOutputStream (ALGORITHMS_PATH + request.algorithm.name + "/data.bin");
                    outputStream.write(data, 0, data.length);
                    outputStream.flush();
                    outputStream.close();
                    String decodedSource = new String(request.algorithm.sourceFile, "UTF-8");
                    writer = new BufferedWriter( new FileWriter(ALGORITHMS_PATH + request.algorithm.name + "/main.cpp"));
                    writer.write(decodedSource);
                    writer.flush();
                    writer.close();
                    String decodedTests = new String(request.algorithm.testFile, "UTF-8");
                    writer = new BufferedWriter( new FileWriter(ALGORITHMS_PATH + request.algorithm.name + "/test_data.txt"));
                    writer.write(decodedTests);
                    writer.flush();
                    writer.close();
                    response.status = "OK";
                } catch(IOException ex) {
                    response.status = "FAIL";
                } finally {
                    try {
                        outputStream.close();
                    } catch(IOException ex){
                        //
                    }
                }

            } else {
                response.status = "FAIL";
            }
        } else {
            response.status = "ALGORITHM ALREADY EXISTS";
        }
        return response;
    }

    private GetAlgorithmResponse process(GetAlgorithmRequest request) {
        FileInputStream fstream = null;
        Algorithm algorithm = null;
        try {
            fstream = new FileInputStream(ALGORITHMS_PATH + request.algorithm.name + "/data.bin");
            ObjectInputStream ostream = new ObjectInputStream(fstream);
            algorithm = (Algorithm)ostream.readObject();
            return new GetAlgorithmResponse(algorithm, "OK");
        } catch(Exception ex) {
            return new GetAlgorithmResponse(request.algorithm, "Fail. Algorithm not found");
        } finally {
            try {
                fstream.close();
            } catch (Exception ex) {

            }
        }
    }
    private FindAlgorithmResponse process(FindAlgorithmRequest request) {
        FileInputStream fstream = null;
        Algorithm algorithm = null;
        File file = new File(ALGORITHMS_PATH);
        ArrayList<Algorithm> result = new ArrayList<>();
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });

        for (int i = 0; i < directories.length; i++) {
            try {
                fstream = new FileInputStream(ALGORITHMS_PATH + directories[i] + "/data.bin");
                ObjectInputStream ostream = new ObjectInputStream(fstream);
                algorithm = (Algorithm)ostream.readObject();
                if (algorithm.tags.containsAll(request.tags)) {
                    result.add(algorithm);
                }
            } catch(Exception ex) {
            }
        }

        return new FindAlgorithmResponse(result, "OK");
    }

    public static byte[] getBytes(Object obj) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            bos.close();
            return bos.toByteArray();
        }
        catch (java.io.IOException ex) {
            return null;
        }
    }
}
