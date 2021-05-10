package de.epsdev.packages;

import de.epsdev.packages.encryption.EncryptionMode;
import de.epsdev.packages.encryption.RSA_Pair;
import de.epsdev.packages.packages.Package;
import de.epsdev.packages.packages.PackageRespondRSA;
import de.epsdev.packages.packages.PackagesSendAndRequestRSA;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;

public class Server extends Thread{
    private ServerSocket serverSocket;
    private HashMap<String,OnPackageReceive> packages = new HashMap<>();
    private final int port;

    private final EncryptionMode mode;

    public Server(int port, int timeout, EncryptionMode mode){
        this.port = port;
        this.mode = mode;

        this.registerPackage(new PackageRespondRSA());
        this.registerPackage(new PackagesSendAndRequestRSA());

        if(mode != null){
            Package.KEYS = new RSA_Pair(mode);
            System.out.println(Package.KEYS.getPublicKey().getEncoded());
        }

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

        if(Package.KEYS != null && !Package.CLIENT_KEYS.containsKey(s.toString())){
            new PackagesSendAndRequestRSA(mode, Package.KEYS.getPublicKey()).send(s);
        }

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
