import de.epsdev.packages.Connection;
import de.epsdev.packages.packages.Package;

public class client {

    public static void main(String [] args)  {

        Package.registerPackage("PackageRequestEcho",PackageRequestEcho.class);
        Package.registerPackage("PackageRespondEcho",PackageRespondEcho.class);

        Connection connection = new Connection("localhost",10101);

        connection.start();

        connection.send(new PackageRequestEcho());

    }
}
