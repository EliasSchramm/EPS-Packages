import de.epsdev.packages.packages.Package;

public class PackagePong extends Package {
    public PackagePong() {
        super("PackagePong", (aPackage, socket) -> System.out.println("Pong"));

        add("love", false);
        add("money", 0);
        add("friends", new String[]{"Fru", "Fra", "Fri"});
        add("sawqeq", "^1");
        add("sawqfffffffeq", "ffffffffffff");
        add("sssssss", 1f);
        add("test_double", 1D);
        add("test_long", 1L);
    }
}
