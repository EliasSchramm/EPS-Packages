package de.epsdev.packages;

import de.epsdev.packages.encryption.EncryptionMode;
import de.epsdev.packages.encryption.HandshakeSequence;
import de.epsdev.packages.encryption.RSA_Pair;
import de.epsdev.packages.packages.*;
import de.epsdev.packages.packages.Package;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Server extends Thread{
    private ServerSocket serverSocket;
    private final int port;
    private final int packageSize;

    private final EncryptionMode mode;
    private List<Socket> connections;
    private List<Socket> toBeDisconnected  = new ArrayList<>();
    private HashMap<String, Long> ping = new HashMap<>();
    private PackageCache packageCache = new PackageCache();

    public Server(int port, int packageSize, boolean do_encrypt){
        this.port = port;
        this.mode = do_encrypt ? EncryptionMode.RSA4086 : null;
        this.packageSize = packageSize;

        registerPackage("PackageKeepAlive", PackageKeepAlive.class);
        registerPackage("PackageRespondKeepAlive", PackageRespondKeepAlive.class);
        registerPackage("PackageServerError", PackageServerError.class);
        registerPackage("PackageRequestDisconnect", PackageRequestDisconnect.class);

        if(mode != null){
            Package.KEYS = new RSA_Pair(mode);
        }

        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(10000);

            this.connections = new ArrayList<>();
            keepAlive().start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void run() {
        while(true) {
            Socket client;
            try {
                client = serverSocket.accept();
                HandshakeSequence.serverSide(client, this.packageSize);

                processConnection(client).start();
            } catch (IOException ignored) {}
        }
    }

    private Thread processConnection(Socket s){
        this.connections.add(s);
        Package.setPackageSize(s, this.packageSize);
        return new Thread(() -> {

            boolean isActive = true;

            while (isActive){
                try {
                    DataInputStream in;
                    in = new DataInputStream(s.getInputStream());
                    String data = in.readUTF();

                    Package received = packageCache.process(data, s);
                    if(received != null) received.onPackageReceive(s, this);

                } catch (IOException ignored) {
                    isActive = false;
                }
            }
        });
    }

    private Thread keepAlive(){
        return new Thread(() -> {
            while (true){
                List<Socket> toBeRemoved = new ArrayList<>();
                for(Socket s : this.connections){
                    try {
                        new PackageKeepAlive(s.toString()).send(s);
                    } catch (IOException ignored) {
                        toBeRemoved.add(s);
                    }
                }

                toBeRemoved.forEach(s -> this.connections.remove(s));

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for(Socket socket : new ArrayList<>(this.connections)){
                    connections.remove(socket);
                }
                this.toBeDisconnected = new ArrayList<>();
            }
        });
    }

    public void send(Package p,Socket s){
        try {
            p.send(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long getPing(Socket s){
        return ping.getOrDefault(s.toString(),0L);
    }

    public void setPing(Socket s, long p){
        if(ping.containsKey(s.toString())){
            ping.replace(s.toString(), p);
            return;
        }

        ping.put(s.toString(), p);
    }

    public List<Socket> getConnections(){
        return this.connections;
    }

    public void disconnectClient(Socket s){
        this.toBeDisconnected.add(s);
    }

    public void registerPackage(String name, Class clazz){
        Package.registerPackage(name, clazz);
    }
}
