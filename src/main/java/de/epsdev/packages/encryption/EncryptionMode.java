package de.epsdev.packages.encryption;

public enum EncryptionMode {
    RSA512,RSA1024,RSA2048,RSA4086;

    public static int getBitValue(EncryptionMode mode){
        switch (mode){
            case RSA512:
                return 512;
            case RSA1024:
                return 1024;
            case RSA2048:
                return 2048;
            case RSA4086:
                return 4086;
        }
        return 0;
    }
}
