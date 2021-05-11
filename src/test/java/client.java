import de.epsdev.packages.Connection;

public class client {

    public static void main(String [] args) {
        Connection r = new Connection("localhost",1010);
        r.registerPackage(new PackagePing());
        r.registerPackage(new PackagePong());
        r.start();

        r.send(new PackagePing());
    }
}
