package de.epsdev.packages;

import de.epsdev.packages.encryption.EncryptionMode;
import de.epsdev.packages.encryption.HandshakeSequence;
import de.epsdev.packages.encryption.RSA_Pair;
import de.epsdev.packages.packages.Package;
import de.epsdev.packages.packages.PackageKeepAlive;
import de.epsdev.packages.packages.PackageRespondKeepAlive;

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

    private final EncryptionMode mode;
    private List<Socket> connections;
    private HashMap<String, Long> ping = new HashMap<>();

    public int test = 0;

    public Server(int port, boolean do_encrypt){
        this.port = port;
        this.mode = do_encrypt ? EncryptionMode.RSA4086 : null;

        registerPackage("PackageKeepAlive", PackageKeepAlive.class);
        registerPackage("PackageRespondKeepAlive", PackageRespondKeepAlive.class);

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
                HandshakeSequence.serverSide(client);

                processConnection(client).start();
            } catch (IOException ignored) {}
        }
    }

    private Thread processConnection(Socket s){
        this.connections.add(s);
        return new Thread(() -> {
            while (true){
                try {
                    DataInputStream in;
                    in = new DataInputStream(s.getInputStream());
                    String data = in.readUTF();

                    Package received = Package.toPackage(data,s);
                    received.onPackageReceive(s, this);
                } catch (IOException ignored) {}
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

                toBeRemoved.forEach(s -> {this.connections.remove(s);});

                System.out.println(test);

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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

    public void registerPackage(String name, Class clazz){
        Package.registerPackage(name, clazz);
    }
}
