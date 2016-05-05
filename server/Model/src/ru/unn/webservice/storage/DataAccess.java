package ru.unn.webservice.storage;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;

public class DataAccess implements IDataAccess {

    private static String DB_PATH = "/home/vvp/distrib/dev/AlgorithmsService_Build/";
    private static String USERS_PATH = DB_PATH + "users/";
    private static String ALGORITHMS_PATH = DB_PATH + "algorithms/";
    private static String STATISTIC_PATH = DB_PATH + "stat/";

    public IDataResponse process(IDataRequest request) {
        if (request instanceof AddUserDataRequest) {
            return (IDataResponse)process((AddUserDataRequest) request);
        } else if (request instanceof GetUserDataRequest){
            return (IDataResponse)process((GetUserDataRequest) request);
        } else if (request instanceof AddAlgorithmDataRequest){
            return (IDataResponse)process((AddAlgorithmDataRequest)request);
        } else if (request instanceof GetAlgorithmDataRequest) {
            return (IDataResponse)process((GetAlgorithmDataRequest)request);
        } else if (request instanceof FindAlgorithmDataRequest) {
            return (IDataResponse)process((FindAlgorithmDataRequest)request);
        } else if (request instanceof GetStatDataRequest) {
            return (IDataResponse)process((GetStatDataRequest)request);
        } else {
            return null;
        }
    }

    private AddUserDataResponse process(AddUserDataRequest request) {
        String status = writeUser(request.user);
        return new AddUserDataResponse(status);
    }

    private GetUserDataResponse process(GetUserDataRequest request) {
        User user = readUser(request.username);
        if (user != null) {
            return new GetUserDataResponse(user, "OK");
        }

        return new GetUserDataResponse(user, "Fail. Object not found");
    }

    private AddAlgorithmDataResponse process(AddAlgorithmDataRequest request) {
        String status = writeAlgorithm(request.algorithm);
        return new AddAlgorithmDataResponse(status);
    }

    private GetAlgorithmDataResponse process(GetAlgorithmDataRequest request) {
        FileInputStream fstream = null;
        Algorithm algorithm = null;
        try {
            fstream = new FileInputStream(ALGORITHMS_PATH + request.algorithm + "/data.bin");
            ObjectInputStream ostream = new ObjectInputStream(fstream);
            algorithm = (Algorithm)ostream.readObject();
            fstream.close();
            ostream.close();

            Statistic statistic = null;
            File f = new File(STATISTIC_PATH + "stat.bin");
            if(!f.exists()) {
                statistic = new Statistic();
            } else {
                fstream = new FileInputStream(STATISTIC_PATH + "stat.bin");
                ostream = new ObjectInputStream(fstream);
                statistic = (Statistic)ostream.readObject();
            }

            statistic.addDownload(Calendar.getInstance().getTime());
            byte[] data = getBytes(statistic);
            OutputStream outputStream = new FileOutputStream (STATISTIC_PATH + "stat.bin");
            outputStream.write(data, 0, data.length);
            outputStream.flush();
            outputStream.close();
            return new GetAlgorithmDataResponse(algorithm, "OK");
        } catch(Exception ex) {
            return new GetAlgorithmDataResponse(algorithm, "Fail. Algorithm not found");
        } finally {
            try {
                fstream.close();
            } catch (Exception ex) {

            }
        }
    }

    private FindAlgorithmDataResponse process(FindAlgorithmDataRequest request) {
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

        for (String directory : directories) {
            try {
                fstream = new FileInputStream(ALGORITHMS_PATH + directory + "/data.bin");
                ObjectInputStream ostream = new ObjectInputStream(fstream);
                algorithm = (Algorithm) ostream.readObject();
                if (algorithm.tags.containsAll(request.tags)) {
                    result.add(algorithm);
                }
            } catch (Exception ex) {
                return new FindAlgorithmDataResponse(result, "FAIL");
            }
        }

        return new FindAlgorithmDataResponse(result, "OK");
    }

    private GetStatDataResponse process(GetStatDataRequest request) {
        FileInputStream fstream = null;
        Statistic statistic = null;
        try {
            fstream = new FileInputStream(STATISTIC_PATH + "stat.bin");
            ObjectInputStream ostream = new ObjectInputStream(fstream);
            statistic = (Statistic)ostream.readObject();
            int downloads = statistic.getDownloadsCount(request.from, request.to);
            int purchases = statistic.getPurchasesCount(request.from, request.to);
            return new GetStatDataResponse(downloads, purchases, "OK");
        } catch(Exception ex) {
            return new GetStatDataResponse(-1, -1, "Fail. Object not found");
        } finally {
            try {
                fstream.close();
            } catch (Exception ex) {

            }
        }
    }

    public String writeAlgorithm(Algorithm algorithm) {
        File path = new File(ALGORITHMS_PATH + algorithm.name);
        if (!path.exists()) {
            if (path.mkdir()) {
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

            } else {
                return "FAIL";
            }
        } else {
            return "ALGORITHM ALREADY EXISTS";
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

    public String writeUser(User user) {
        File path = new File(USERS_PATH + user.login);
        if (!path.exists()) {
            if (path.mkdir()) {
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
            } else {
                return "FAIL";
            }
        } else {
            return "USER ALREADY EXISTS";
        }
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

    public void deleteUsers() {
        File directory = new File(USERS_PATH);
        deleteDirectory(directory);
        directory.mkdir();
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

    private static boolean deleteDirectory(File directory) {
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
