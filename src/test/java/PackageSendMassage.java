import de.epsdev.packages.packages.Base_Package;
import de.epsdev.packages.packages.Package;

import java.io.IOException;
import java.net.Socket;

public class PackageSendMassage extends Package {
    public PackageSendMassage() {
        super("PackageSendMassage");
    }

    public PackageSendMassage(Base_Package p) {
        super(p);
    }

    public void setMSG(String msg){
        add("msg", msg);
    }

    @Override
    public void onPackageReceive(Socket sender, Object o) {
        PackageRespondEncrypted p_resp = new PackageRespondEncrypted();
        p_resp.setMessage(getString("msg"));
        try {
            p_resp.send(sender);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
