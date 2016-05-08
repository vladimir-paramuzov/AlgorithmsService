package ru.unn.webservice.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class Connector extends Thread {
    Connector(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        try {
            final int port = 6666;
            ServerSocket serverSocket = new ServerSocket(port);
            while (!Thread.currentThread().isInterrupted()) {
                Socket clientSocket = serverSocket.accept();

                ClientConnection clientConnection = new ClientConnection(clientSocket, server);
                clientConnection.start();
            }
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
    }

    private final Server server;
}
