package ru.unn.webservice.storage;

import java.io.*;
import java.util.ArrayList;

public class DataAccess implements IDataAccess {

    private static String DB_PATH = "/home/vvp/distrib/dev/AlgorithmsService_Build/";
    private static String USERS_PATH = DB_PATH + "users/";
    private static String ALGORITHMS_PATH = DB_PATH + "algorithms/";
    private static String STATISTIC_PATH = DB_PATH + "stat/";

    public IDataResponse process(IDataRequest request) {
        if (request instanceof StoreUserDataRequest) {
            return (IDataResponse)process((StoreUserDataRequest) request);
        } else if (request instanceof StoreAlgorithmDataRequest){
            return (IDataResponse)process((StoreAlgorithmDataRequest)request);
        } else if (request instanceof StoreStatDataRequest){
            return (IDataResponse)process((StoreStatDataRequest)request);
        } else if (request instanceof LoadUserDataRequest){
            return (IDataResponse)process((LoadUserDataRequest) request);
        } else if (request instanceof LoadAlgorithmDataRequest) {
            return (IDataResponse)process((LoadAlgorithmDataRequest)request);
        } else if (request instanceof LoadStatDataRequest) {
            return (IDataResponse)process((LoadStatDataRequest)request);
        } else if (request instanceof LoadUsersListDataRequest) {
            return (IDataResponse)process((LoadUsersListDataRequest)request);
        } else if (request instanceof LoadAlgorithmsListDataRequest) {
            return (IDataResponse)process((LoadAlgorithmsListDataRequest)request);
        } else {
            return null;
        }
    }

    private StoreUserDataResponse process(StoreUserDataRequest request) {
        String status = writeUser(request.user);
        return new StoreUserDataResponse(status);
    }

    private StoreAlgorithmDataResponse process(StoreAlgorithmDataRequest request) {
        String status = writeAlgorithm(request.algorithm);
        return new StoreAlgorithmDataResponse(status);
    }

    private StoreStatDataResponse process(StoreStatDataRequest request) {
        String status = writeStatistic(request.statistic);
        return new StoreStatDataResponse(status);
    }

    private LoadUserDataResponse process(LoadUserDataRequest request) {
        User user = readUser(request.username);
        if (user != null) {
            return new LoadUserDataResponse(user, "OK");
        }

        return new LoadUserDataResponse(user, "Fail. Object not found");
    }

    private LoadAlgorithmDataResponse process(LoadAlgorithmDataRequest request) {
        Algorithm algorithm = readAlgorithm(request.algorithmName);
        if (algorithm == null) {
            return new LoadAlgorithmDataResponse(null, "FAIL");
        } else {
            return new LoadAlgorithmDataResponse(algorithm, "OK");
        }
    }

    private LoadStatDataResponse process(LoadStatDataRequest request) {
        Statistic statistic = readStatistic();
        if (statistic == null) {
            return new LoadStatDataResponse(null, "FAIL");
        } else {
            return new LoadStatDataResponse(statistic, "OK");
        }
    }

    private LoadUsersListDataResponse process(LoadUsersListDataRequest request) {
        ArrayList<User> usersList = new ArrayList<>();

        File file = new File(USERS_PATH);
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });

        for (String directory : directories) {
            User user = readUser(directory);
            if (user == null) {
                return new LoadUsersListDataResponse(null, "FAIL");
            }
            usersList.add(user);
        }

        return new LoadUsersListDataResponse(usersList, "OK");
    }

    private LoadAlgorithmsListDataResponse process(LoadAlgorithmsListDataRequest request) {
        ArrayList<Algorithm> algorithmsList = new ArrayList<>();

        File file = new File(ALGORITHMS_PATH);
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });

        for (String directory : directories) {
            Algorithm algorithm = readAlgorithm(directory);
            if (algorithm == null) {
                return new LoadAlgorithmsListDataResponse(null, "FAIL");
            }
            algorithmsList.add(algorithm);
        }

        return new LoadAlgorithmsListDataResponse(algorithmsList, "OK");
    }

    public User readUser(String username) {
        FileInputStream fstream = null;
        User user = null;
        try {
            fstream = new FileInputStream(USERS_PATH + username + "/data.bin");
            ObjectInputStream ostream = new ObjectInputStream(fstream);
            user = (User)ostream.readObject();
            fstream.close();
            return user;
        } catch(Exception ex) {
            return null;
        }
    }

    public Algorithm readAlgorithm(String algorithmName) {
        try {
            FileInputStream fstream = new FileInputStream(ALGORITHMS_PATH + algorithmName + "/data.bin");
            ObjectInputStream ostream = new ObjectInputStream(fstream);
            Algorithm algorithm = (Algorithm)ostream.readObject();
            fstream.close();
            ostream.close();

            return algorithm;
        } catch(Exception ex) {
            return null;
        }
    }

    public Statistic readStatistic() {
        Statistic statistic = null;

        try {
            File f = new File(STATISTIC_PATH + "stat.bin");
            if(!f.exists()) {
                statistic = new Statistic();
            } else {
                FileInputStream fstream = new FileInputStream(STATISTIC_PATH + "stat.bin");
                ObjectInputStream ostream = new ObjectInputStream(fstream);
                statistic = (Statistic)ostream.readObject();
                ostream.close();
                fstream.close();
            }
        } catch (Exception ex) {
            return null;
        }

        return statistic;
    }

    public String writeUser(User user) {
        File path = new File(USERS_PATH + user.login);
        if (!path.exists()) {
            if (!path.mkdir()) {
                return "FAIL";
            }
        }

        try {
            byte[] data = getBytes(user);
            OutputStream outputStream = new FileOutputStream(USERS_PATH + user.login + "/data.bin");
            outputStream.write(data, 0, data.length);
            outputStream.flush();
            outputStream.close();
            return "OK";
        } catch (IOException ex) {
            return "FAIL";
        }
    }

    public String writeAlgorithm(Algorithm algorithm) {
        File path = new File(ALGORITHMS_PATH + algorithm.name);
        if (!path.exists()) {
            if (!path.mkdir()) {
                return "FAIL";
            }
        }

        try {
            byte[] data = getBytes(algorithm);
            OutputStream outputStream = new FileOutputStream (ALGORITHMS_PATH + algorithm.name + "/data.bin");
            outputStream.write(data, 0, data.length);
            outputStream.flush();
            outputStream.close();
            String decodedSource = new String(algorithm.sourceFile, "UTF-8");
            BufferedWriter writer = new BufferedWriter( new FileWriter(ALGORITHMS_PATH + algorithm.name + "/main.cpp"));
            writer.write(decodedSource);
            writer.flush();
            writer.close();
            String decodedTests = new String(algorithm.testFile, "UTF-8");
            writer = new BufferedWriter( new FileWriter(ALGORITHMS_PATH + algorithm.name + "/test_data.txt"));
            writer.write(decodedTests);
            writer.flush();
            writer.close();
            //outputStream.close();
            return "OK";
        } catch(Exception ex) {
            return "FAIL";
        }
    }

    public String writeStatistic(Statistic statistic) {
        try {
            byte[] data = getBytes(statistic);
            OutputStream outputStream = new FileOutputStream(STATISTIC_PATH + "stat.bin");
            outputStream.write(data, 0, data.length);
            outputStream.flush();
            outputStream.close();
            return "OK";
        } catch(Exception ex) {
            return "FAIL";
        }
    }

    public void deleteAlgorithms() {
        File directory = new File(ALGORITHMS_PATH);
        deleteDirectory(directory);
        directory.mkdir();
    }

    public void deleteStatistic() {
        File directory = new File(STATISTIC_PATH);
        deleteDirectory(directory);
        directory.mkdir();
    }


    public void deleteUsers() {
        File directory = new File(USERS_PATH);
        deleteDirectory(directory);
        directory.mkdir();
    }

    static byte[] getBytes(Object obj) {
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
}
