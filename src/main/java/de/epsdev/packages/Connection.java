package de.epsdev.packages;

import de.epsdev.packages.packages.Package;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Connection extends Thread{
    private Socket socket;
    private final int port;

    public Connection(String host, int port){
        this.port = port;
        try {
            this.socket = new Socket(host, port);
            HandshakeSequence.clientSide(socket);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void init(){
        this.run();
    }

    public void run() {
        while(true) {
            try {
                DataInputStream in;
                in = new DataInputStream(socket.getInputStream());

                String data = in.readUTF();

                Package received = Package.toPackage(data,socket);
                received.onPackageReceive(socket);

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

    public void registerPackage(String name, Class clazz){
        Package.registerPackage(name, clazz);
    }
}
