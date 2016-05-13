package ru.unn.webservice.server;

import ru.unn.webservice.infrastructure.*;

import java.io.*;
import java.net.Socket;

class ClientConnection extends Thread {
    ClientConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            while (socket.isConnected()) {
                IRequest request = (IRequest)ois.readObject();
                if (request != null) {
                    if ((request instanceof AuthorizationRequest) ||
                        (request instanceof RegisterRequest)) {
                       oos.writeObject(server.getAuthorizationSystem().process(request));
                    } else if ((request instanceof AddAlgorithmRequest) ||
                               (request instanceof BuyAlgorithmRequest) ||
                               (request instanceof DownloadAlgorithmRequest) ||
                               (request instanceof SearchAlgorithmRequest  )) {
                        oos.writeObject(server.getAlgorithmControlSystem().process(request));
                    } else if (request instanceof ChangeBalanceRequest) {
                        oos.writeObject(server.getPaymentSystem().process(request));
                    } else if (request instanceof GetStatisticRequest) {
                        oos.writeObject(server.getStatisticCollector().process(request));
                    } else if (request instanceof TestAlgorithmRequest) {
                        oos.writeObject(server.getBuildbot().process(request));
                    }
//                    oos.flush();
                }
            }
            ois.close();
            oos.close();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    private Socket socket;
    private final Server server;
}
