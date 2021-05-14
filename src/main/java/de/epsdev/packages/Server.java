package de.epsdev.packages;

import de.epsdev.packages.encryption.EncryptionMode;
import de.epsdev.packages.encryption.RSA_Pair;
import de.epsdev.packages.packages.Base_Package;
import de.epsdev.packages.packages.Package;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;

public class Server extends Thread{
    private ServerSocket serverSocket;
    private final int port;

    private final EncryptionMode mode;

    public Server(int port, int timeout, EncryptionMode mode){
        this.port = port;
        this.mode = mode;

        if(mode != null){
            Package.KEYS = new RSA_Pair(mode);
        }

        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(timeout);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void init(){
        this.run();
    }

    public void run() {
        System.out.println("Waiting for client on port " +
                serverSocket.getLocalPort() + "...");
        while(true) {
            Socket client;
            try {
                client = serverSocket.accept();
                HandshakeSequence.serverSide(client);

                System.out.println("Handshake Ready");
                processConnection(client).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Thread processConnection(Socket s){
        return new Thread(() -> {
            while (true){
                try {
                    DataInputStream in;
                    in = new DataInputStream(s.getInputStream());
                    String data = in.readUTF();

                    Package received = Package.toPackage(data,s);
                    received.onPackageReceive(s);
                } catch (SocketTimeoutException e) {
                    System.out.println("Socket timed out!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void send(Package p,Socket s){
        p.send(s);
    }

    public void registerPackage(String name, Class clazz){
        Package.registerPackage(name, clazz);
    }
}
