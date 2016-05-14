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
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

    }

    public ServerConnection(String ip) throws Exception {
        final int port = 9999;
        InetAddress addr = InetAddress.getByName(ip);
        socket = new Socket(addr, port);
        oos = new ObjectOutputStream(socket.getOutputStream());
        oos.flush();
        ois = new ObjectInputStream(socket.getInputStream());
    }

    public IResponse send(IRequest request) {
        try {
            oos.writeObject(request);
            return (IResponse)ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Socket socket;
}
