package de.epsdev.packages.packages;

import de.epsdev.packages.Server;

import java.net.Socket;

public class PackageRespondKeepAlive extends Package{
    public PackageRespondKeepAlive(Base_Package base_package) {
        super(base_package);
    }

    public PackageRespondKeepAlive(long timestamp, String socket_details) {
        super("PackageRespondKeepAlive");
        add("creation_time", timestamp);
        add("socket_details", socket_details);
    }

    @Override
    public void onPackageReceive(Socket sender, Object o) {
        long unixTime = System.currentTimeMillis();
        if(o instanceof Server) ((Server) o).setPing(sender, unixTime - getLong("creation_time"));
    }
}
