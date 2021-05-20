package de.epsdev.packages;

import de.epsdev.packages.encryption.HandshakeSequence;
import de.epsdev.packages.packages.Package;
import de.epsdev.packages.packages.*;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection extends Thread {
    private Socket socket;
    private final int port;
    private PackageCache packageCache = new PackageCache();

    public Connection(String host, int port) {
        this.port = port;

        registerPackage("PackageKeepAlive", PackageKeepAlive.class);
        registerPackage("PackageRespondKeepAlive", PackageRespondKeepAlive.class);
        registerPackage("PackageServerError", PackageServerError.class);
        registerPackage("PackageRequestDisconnect", PackageRequestDisconnect.class);

        try {
            this.socket = new Socket(host, port);
            int package_size = HandshakeSequence.clientSide(socket);
            Package.setPackageSize(socket, package_size);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                DataInputStream in;
                in = new DataInputStream(socket.getInputStream());

                String data = in.readUTF();

                Package received = packageCache.process(data, socket);
                if (received != null) received.onPackageReceive(socket, this);
            } catch (IOException ignored) {
            }
        }
    }

    public void close() {
        try {
            new PackageRequestDisconnect().send(socket);
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPort() {
        return this.port;
    }

    public void send(Package p) {
        try {
            p.send(this.socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getServerSocket() {
        return this.socket;
    }

    public void registerPackage(String name, Class clazz) {
        Package.registerPackage(name, clazz);
    }
}
