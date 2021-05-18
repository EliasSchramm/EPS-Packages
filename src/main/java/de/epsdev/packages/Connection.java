package de.epsdev.packages;

import de.epsdev.packages.encryption.HandshakeSequence;
import de.epsdev.packages.packages.Package;
import de.epsdev.packages.packages.PackageKeepAlive;
import de.epsdev.packages.packages.PackageRespondKeepAlive;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection extends Thread{
    private Socket socket;
    private final int port;

    public Connection(String host, int port){
        this.port = port;

        registerPackage("PackageKeepAlive", PackageKeepAlive.class);
        registerPackage("PackageRespondKeepAlive", PackageRespondKeepAlive.class);

        try {
            this.socket = new Socket(host, port);
            HandshakeSequence.clientSide(socket);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void run() {
        while(true) {
            try {
                DataInputStream in;
                in = new DataInputStream(socket.getInputStream());

                String data = in.readUTF();

                Package received = Package.toPackage(data,socket);
                received.onPackageReceive(socket, this);
            } catch (IOException ignored) {}
        }
    }

    public void close(){
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPort(){
        return this.port;
    }

    public void send(Package p){
        try {
            p.send(this.socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerPackage(String name, Class clazz){
        Package.registerPackage(name, clazz);
    }
}
