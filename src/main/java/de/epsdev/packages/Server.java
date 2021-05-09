package de.epsdev.packages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;

public class Server extends Thread{
    private ServerSocket serverSocket;
    private HashMap<String,OnPackageReceive> packages = new HashMap<>();
    private final int port;

    public Server(int port, int timeout){
        this.port = port;
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(timeout);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void run() {
        System.out.println("Waiting for client on port " +
                serverSocket.getLocalPort() + "...");
        while(true) {
            Socket client;
            try {
                client = serverSocket.accept();
                processConnection(client).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Thread processConnection(Socket s){
        return new Thread(() -> {
            try {
                System.out.println("Just connected to " + s.getRemoteSocketAddress());

                DataInputStream in;
                in = new DataInputStream(s.getInputStream());

                String data = in.readUTF();
                Package received = new Package(data);

                this.packages.get(received.getName()).onReceive(received, s);

            } catch (SocketTimeoutException e) {
                System.out.println("Socket timed out!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void send(Package p,Socket s){
        p.send(s);
    }

    public void registerPackage(Package p){
        if(!this.packages.containsKey(p.getName()))
            this.packages.put(p.getName(), p.onPackageReceive);
    }
}
