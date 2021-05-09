import de.epsdev.packages.Server;

public class server1 {

    public static void main(String [] args) {
        Server s = new Server(1010, 10000);
        s.registerPackage(new PackagePing());
        s.registerPackage(new PackagePong());
        s.start();
    }
}
