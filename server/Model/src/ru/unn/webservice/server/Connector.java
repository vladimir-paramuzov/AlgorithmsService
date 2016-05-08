package ru.unn.webservice.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

class Connector extends Thread {
    Connector(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        try {
            final int port = 9999;
            serverSocket = new ServerSocket(port, 0, InetAddress.getLocalHost());
            while (!isInterrupted()) {
                Socket clientSocket = serverSocket.accept();

                ClientConnection clientConnection = new ClientConnection(clientSocket, server);
                clientConnection.start();
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            Thread.currentThread().interrupt();
        }
    }

    public void close() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private ServerSocket serverSocket;
    private final Server server;
}
