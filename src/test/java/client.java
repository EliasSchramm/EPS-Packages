import de.epsdev.packages.Connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class client {

    public static void main(String [] args)  {
        Connection r = new Connection("localhost",1010);

        r.registerPackage("PackageSendMassage",PackageSendMassage.class);
        r.registerPackage("PackageRespondEncrypted",PackageRespondEncrypted.class);

        r.start();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true){
            System.out.print("Enter a msg to encrypt: ");
            try {
                String s = br.readLine();
                PackageSendMassage p = new PackageSendMassage();
                p.setMSG(s);
                r.send(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
