import de.epsdev.packages.packages.Package;

public class PackagePing extends Package {
    public PackagePing() {
        super("PackagePing", (p, s) -> {
            System.out.println("ping");
            new PackagePong().send(s);
        });
    }
}
