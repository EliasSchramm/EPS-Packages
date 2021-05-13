package de.epsdev.packages.packages;

import de.epsdev.packages.encryption.AES_Key;
import de.epsdev.packages.encryption.RSA_Pair;

import java.lang.reflect.Constructor;
import java.net.Socket;
import java.util.*;

public abstract class Package extends Base_Package{
    public static RSA_Pair KEYS = null;
    public static HashMap<String, AES_Key> CLIENT_KEYS = new HashMap<>();
    public static HashMap<String, Class> PACKAGES = new HashMap<>();

    public Package(String base64, Socket s) {
        super(base64, s);
    }

    public Package(String name) {
        super(name);
    }

    public Package(Base_Package base_package) {
        super(base_package);
    }

    public abstract void onPackageReceive(Socket sender);

    public static void registerPackage(String name, Class packageClass) {
        if(!PACKAGES.containsKey(name)) PACKAGES.put(name, packageClass);
    }

    public static Package toPackage(String base64, Socket s){
        Base_Package p = new Base_Package(base64, s);
        if(!PACKAGES.containsKey(p.getName())) return null;
        try {
            Class<Package> clazz = PACKAGES.get(p.getName());
            Constructor<?> cons = clazz.getConstructor(Base_Package.class);
            return clazz.cast(cons.newInstance(p));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

}
