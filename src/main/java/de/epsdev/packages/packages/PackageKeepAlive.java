package de.epsdev.packages.packages;

import java.io.IOException;
import java.net.Socket;

public class PackageKeepAlive extends Package{
    public PackageKeepAlive(Base_Package base_package) {
        super(base_package);
    }

    public PackageKeepAlive(String socket_details) {
        super("PackageKeepAlive");
        long unixTime = System.currentTimeMillis();
        add("creation_time", unixTime);
        add("socket_details", socket_details);
    }

    @Override
    public void onPackageReceive(Socket sender, Object o) {
        try {
            new PackageRespondKeepAlive(getLong("creation_time"), getString("socket_details")).send(sender);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
