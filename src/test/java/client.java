import de.epsdev.packages.Connection;
import de.epsdev.packages.encryption.EncryptionMode;
import de.epsdev.packages.encryption.RSA_Pair;

import java.security.KeyPair;
import java.security.PublicKey;

public class client {

    public static void main(String [] args) {
        Connection r = new Connection("localhost",1010);
        r.registerPackage(new PackagePing());
        r.registerPackage(new PackagePong());
        r.start();

        r.send(new PackagePing());
    }
}
