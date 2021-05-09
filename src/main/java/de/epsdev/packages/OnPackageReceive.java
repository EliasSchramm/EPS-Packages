package de.epsdev.packages;

import java.net.Socket;

public interface OnPackageReceive {
    void onReceive(Package p, Socket s);
}
