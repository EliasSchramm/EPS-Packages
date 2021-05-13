import de.epsdev.packages.encryption.AES_Key;
import de.epsdev.packages.packages.Base_Package;
import de.epsdev.packages.packages.Package;

import java.net.Socket;

public class PackageRespondEncrypted extends Package {

    public PackageRespondEncrypted() {
        super("PackageRespondEncrypted");
    }

    public PackageRespondEncrypted(Base_Package p) {
        super(p);
    }

    public void setMessage(String s){
        AES_Key k = new AES_Key();
        add("key", k.toString());
        add("msg", k.encrypt(s));
    }

    @Override
    public void onPackageReceive(Socket sender) {
        String s_key = getString("key");
        AES_Key k = new AES_Key(s_key);

        String msg = getString("msg");

        System.out.println("------------------");
        System.out.println("Key: " + s_key);
        System.out.println("Encrypted string: " + msg);
        System.out.println("Decrypted String: " + k.decrypt(msg) );
        System.out.println("------------------");
    }
}
