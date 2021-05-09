import de.epsdev.packages.Package;

public class PackagePong extends Package {
    public PackagePong() {
        super("PackagePong", (aPackage, socket) -> System.out.println("Pong"));

        add("love", false);
        add("money", 0);
        add("friends", new String[]{"Fru", "Fra", "Fri"});
        add("name", "ssssss");
    }
}
