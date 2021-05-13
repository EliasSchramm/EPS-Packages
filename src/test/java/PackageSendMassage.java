import de.epsdev.packages.packages.Base_Package;
import de.epsdev.packages.packages.Package;

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
    public void onPackageReceive(Socket sender) {
        PackageRespondEncrypted p_resp = new PackageRespondEncrypted();
        p_resp.setMessage(getString("msg"));
        p_resp.send(sender);
    }
}
