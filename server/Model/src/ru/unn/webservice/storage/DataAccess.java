package ru.unn.webservice.storage;

import ru.unn.webservice.authorization.AuthorizationRequest;
import ru.unn.webservice.authorization.AuthorizationResponse;
import ru.unn.webservice.authorization.RegisterRequest;

import java.io.*;

public class DataAccess implements IDataAccess {
    public IDataResponse process(IDataRequest request) {
        if (request instanceof AddUserRequest) {
            return (IDataResponse)process((AddUserRequest) request);
        } else if (request instanceof GetUserRequest){
            return (IDataResponse)process((GetUserRequest) request);
        } else {
            return null;
        }
    }

    private AddUserResponse process(AddUserRequest request) {
        AddUserResponse response = new AddUserResponse();
        File path = new File("/home/slowpoke/AlgorithmService_Build/users/" + request.user.login);
        if (!path.exists()) {
            if (path.mkdir()) {
                OutputStream outputStream = null;
                try {
                    byte[] data = getBytes(request.user);
                    outputStream = new FileOutputStream ("/home/slowpoke/AlgorithmService_Build/users/" + request.user.login + "/data.bin");
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
            response.status = "USER_ALREADY_EXISTS";
        }
        return response;
    }

    public GetUserResponse process(GetUserRequest request) {
        GetUserRequest rq = null;
        FileInputStream fstream = null;
        User user = null;
        try {
            fstream = new FileInputStream("/home/slowpoke/AlgorithmService_Build/users/" + request.user.login + "/data.bin");
            ObjectInputStream ostream = new ObjectInputStream(fstream);
            try {
                user = (User)ostream.readObject();
            } catch (EOFException e) {

            } catch (ClassNotFoundException ex) {

            }
            return new GetUserResponse(user, "OK");
        } catch(java.io.IOException ex) {

        } finally {
            try {
                fstream.close();
            } catch (java.io.IOException ex) {

            }
        }
        return new GetUserResponse(request.user, "FAIL");
    }

    private static byte[] getBytes(Object obj) throws java.io.IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.flush();
        oos.close();
        bos.close();
        return bos.toByteArray();
    }
}
