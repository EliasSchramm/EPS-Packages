package de.epsdev.packages.packages;

import de.epsdev.packages.encryption.RSA_Pair;

import java.security.PublicKey;

public class PackageRespondRSA extends Package{
    public PackageRespondRSA(PublicKey key) {
        super("PackageRespondRSA", (p, s) -> {
            Package.CLIENT_KEYS.put(s.toString(), RSA_Pair.stringToPublic(p.getString("key")));
            System.out.println("Got key back " + p.getString("key"));
        });

        add("key", RSA_Pair.publicToString(key));
    }

    public PackageRespondRSA(){
        super("PackageRespondRSA", (p, s) -> {
            Package.CLIENT_KEYS.put(s.toString(), RSA_Pair.stringToPublic(p.getString("key")));
            System.out.println("Got key back " + p.getString("key"));
        });

    }
}
