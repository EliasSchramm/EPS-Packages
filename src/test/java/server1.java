import de.epsdev.packages.encryption.EncryptionMode;
import de.epsdev.packages.Server;

public class server1 {

    public static void main(String [] args) {
        Server s = new Server(1010, 10000, EncryptionMode.RSA4086);

        s.registerPackage("PackageSendMassage",PackageSendMassage.class);
        s.registerPackage("PackageRespondEncrypted",PackageRespondEncrypted.class);

        s.start();
    }
}
