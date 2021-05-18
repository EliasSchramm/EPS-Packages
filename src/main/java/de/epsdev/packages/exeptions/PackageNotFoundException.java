package de.epsdev.packages.exeptions;

public class PackageNotFoundException extends Exception{
    public PackageNotFoundException(String packageName){
        super("Received package that could not be retrieved from the register. It may wasn't registered or got corrupted. (" + packageName + ")");
    }
}
