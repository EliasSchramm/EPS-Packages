import de.epsdev.packages.OnPackageReceive;
import de.epsdev.packages.Package;

import java.net.Socket;

public class PackagePing extends Package {
    public PackagePing() {
        super("PackagePing", (p, s) -> {
            System.out.println("ping");
            new PackagePong().send(s);
        });
    }
}
