package de.epsdev.packages.packages;

import de.epsdev.packages.OnPackageReceive;
import de.epsdev.packages.encryption.EncryptionMode;
import de.epsdev.packages.encryption.RSA_Pair;

import java.net.Socket;
import java.security.PublicKey;

public class PackagesSendAndRequestRSA extends Package{
    public PackagesSendAndRequestRSA(EncryptionMode mode, PublicKey publicKey) {

        super("PackagesSendAndRequestRSA", (p, s) -> {
            System.out.println("Generating");

            Package.KEYS = new RSA_Pair(EncryptionMode.valueOf(p.getString("mode")));
            Package.CLIENT_KEYS.put(s.toString(), RSA_Pair.stringToPublic(p.getString("key")));

            new PackageRespondRSA(Package.KEYS.getPublicKey()).send(s);
        });

        if(mode != null){
            add("mode", mode.toString());
            add("key", RSA_Pair.publicToString(publicKey));
        }
    }

    public PackagesSendAndRequestRSA(){
        super("PackagesSendAndRequestRSA", (p, s) -> {
            System.out.println("Generating");

            Package.KEYS = new RSA_Pair(EncryptionMode.valueOf(p.getString("mode")));
            Package.CLIENT_KEYS.put(s.toString(), RSA_Pair.stringToPublic(p.getString("key")));

            new PackageRespondRSA(Package.KEYS.getPublicKey()).send(s);
        });
    }
}
