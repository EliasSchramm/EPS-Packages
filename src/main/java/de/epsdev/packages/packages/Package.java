package de.epsdev.packages.packages;

import de.epsdev.packages.encryption.AES_Key;
import de.epsdev.packages.encryption.RSA_Pair;
import de.epsdev.packages.exeptions.PackageNotFoundException;

import java.lang.reflect.Constructor;
import java.net.Socket;
import java.util.*;

public abstract class Package extends Base_Package{
    public static RSA_Pair KEYS = null;
    public static HashMap<String, AES_Key> CLIENT_KEYS = new HashMap<>();
    public static HashMap<String, Class> PACKAGES = new HashMap<>();
    private static HashMap<String, Integer> PACKAGE_SIZES = new HashMap<>();

    public Package(String base64, Socket s) {
        super(base64, s);
    }

    public Package(String name) {
        super(name);
    }

    public Package(Base_Package base_package) {
        super(base_package);
    }

    public abstract void onPackageReceive(Socket sender, Object o);

    public static void registerPackage(String name, Class packageClass) {
        if(!PACKAGES.containsKey(name)) PACKAGES.put(name, packageClass);
    }

    public static Package toPackage(String base64, Socket s){
        Base_Package p = new Base_Package(base64, s);

        try {
            if(!PACKAGES.containsKey(p.getName())) throw new PackageNotFoundException(p.getName());

            Class<Package> clazz = PACKAGES.get(p.getName());
            Constructor<?> cons = clazz.getConstructor(Base_Package.class);
            return clazz.cast(cons.newInstance(p));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    public static int getPackageSize(Socket s){
        return PACKAGE_SIZES.getOrDefault(s.toString(), 0);
    }

    public static void setPackageSize(Socket s, int size){
        PACKAGE_SIZES.put(s.toString(), size);
    }

}
