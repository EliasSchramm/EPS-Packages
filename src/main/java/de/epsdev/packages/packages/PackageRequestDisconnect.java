package de.epsdev.packages.packages;

import de.epsdev.packages.Server;

import java.net.Socket;

public class PackageRequestDisconnect extends Package{
    public PackageRequestDisconnect(Base_Package base_package) {
        super(base_package);
    }

    public PackageRequestDisconnect() {
        super("PackageRequestDisconnect");
    }

    @Override
    public void onPackageReceive(Socket sender, Object o) {
        if(o instanceof Server){
            ((Server) o).disconnectClient(sender);
        }
    }
}
