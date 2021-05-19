package de.epsdev.packages.exeptions;

public class PackageServerErrorException extends Exception {
    public PackageServerErrorException(String error){
        super(error);
    }
}
