package ru.unn.webservice.client;

import ru.unn.webservice.infrastructure.IRequest;
import ru.unn.webservice.infrastructure.IResponse;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ServerConnection {
    public ServerConnection() {
        try {
            final int port = 9999;
            socket = new Socket(InetAddress.getLocalHost(), port);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

    }

    public IResponse send(IRequest request) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            oos.writeObject(request);
            return (IResponse)ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

    private Socket socket;
}
