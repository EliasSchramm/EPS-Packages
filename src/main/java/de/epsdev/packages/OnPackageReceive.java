package de.epsdev.packages;

import de.epsdev.packages.packages.Package;

import java.net.Socket;

public interface OnPackageReceive {
    void onReceive(Package p, Socket s);
}
