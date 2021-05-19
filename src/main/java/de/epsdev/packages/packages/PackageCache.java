package de.epsdev.packages.packages;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.TreeMap;

public class PackageCache {

    public HashMap<Integer, TreeMap<Integer,String>> cache = new HashMap<>();

    public PackageCache(){

    }

    public Package process(String s, Socket socket){
        String[] metadata = s.split("\\|");

        int part_n = Integer.parseInt(metadata[0]);
        int total_parts = Integer.parseInt(metadata[1]);
        int id = Integer.parseInt(metadata[2]);
        String data = metadata[3];

        if (data.length() > Package.getPackageSize(socket)){
            try {
                new PackageServerError("Package bigger than allowed. Handshake might got corrupted.").send(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        int msgs_in_cache;
        TreeMap<Integer, String> sub_cache;
        if(cache.containsKey(id)){
            sub_cache = cache.get(id);
            sub_cache.put(part_n, data);
            cache.replace(id, sub_cache);

            msgs_in_cache = sub_cache.size();
        }else {
            sub_cache = new TreeMap<>();
            sub_cache.put(part_n, data);
            cache.put(id, sub_cache);

            msgs_in_cache = 1;
        }

        //System.out.println("Received Part " + part_n + " of " + total_parts + " (" + id + ")");

        if(msgs_in_cache == total_parts){
            String combined = "";
            cache.remove(id);

            for(String part : sub_cache.values()) combined += part;

            return Package.toPackage(combined,socket);
        }

        return null;
    }

}
