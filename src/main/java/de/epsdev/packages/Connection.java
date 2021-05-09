package de.epsdev.packages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;

public class Connection extends Thread{
    private Socket socket;
    private HashMap<String,OnPackageReceive> packages = new HashMap<>();
    private final int port;

    public Connection(String host, int port){
        this.port = port;
        try {
            this.socket = new Socket(host, port);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void run() {
        while(true) {
            try {
                System.out.println("Just connected to " + socket.getRemoteSocketAddress());

                DataInputStream in;
                in = new DataInputStream(socket.getInputStream());

                String data = in.readUTF();
                Package received = new Package(data);

                this.packages.get(received.getName()).onReceive(received, socket);

                break;
            } catch (SocketTimeoutException e) {
                System.out.println("Socket timed out!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void send(Package p){
        p.send(this.socket);
    }

    public void registerPackage(Package p){
        if(!this.packages.containsKey(p.getName()))
            this.packages.put(p.getName(), p.onPackageReceive);
    }
}
