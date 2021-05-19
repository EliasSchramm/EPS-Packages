package de.epsdev.packages.packages;

import de.epsdev.packages.Connection;
import de.epsdev.packages.exeptions.PackageServerErrorException;

import java.net.Socket;

public class PackageServerError extends Package{
    public PackageServerError(Base_Package base_package) {
        super(base_package);
    }

    public PackageServerError(String err){
        super("PackageServerError");
        setError(err);
    }

    public void setError(String err){
        add("error", err);
    }

    @Override
    public void onPackageReceive(Socket sender, Object o) {
        if(o instanceof Connection) try {
            throw new PackageServerErrorException(getString("error"));
        } catch (PackageServerErrorException e) {
            e.printStackTrace();
        }
    }
}
