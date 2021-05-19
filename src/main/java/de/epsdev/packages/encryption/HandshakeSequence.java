package de.epsdev.packages.encryption;

import de.epsdev.packages.packages.Package;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.PublicKey;

public class HandshakeSequence {
    public static void serverSide(Socket s, int package_size){
        try {
            OutputStream outToServer = s.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            if(Package.KEYS != null) out.writeUTF(RSA_Pair.publicToString(Package.KEYS.getPublicKey()) + "|" + package_size);
            else{
                out.writeUTF("null|" + package_size);
                return;
            }

            DataInputStream in;
            in = new DataInputStream(s.getInputStream());

            String data = in.readUTF();
            data = RSA_Pair.decrypt(data, Package.KEYS.getPrivateKey());
            Package.CLIENT_KEYS.put(s.toString(), new AES_Key(data));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static int clientSide(Socket s){
        try {
            DataInputStream in;
            in = new DataInputStream(s.getInputStream());

            String raw = in.readUTF();
            String[] data = raw.split("\\|");

            if(data[0].equalsIgnoreCase("null")) return Integer.parseInt(data[1]);

            PublicKey rsa_key = RSA_Pair.stringToPublic(data[0]);
            AES_Key key = new AES_Key();

            OutputStream outToServer = s.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            out.writeUTF(RSA_Pair.encrypt(key.toString(), rsa_key));

            Package.CLIENT_KEYS.put(s.toString(), key);
            Package.KEYS = new RSA_Pair();

            return Integer.parseInt(data[1]);

        }catch (Exception e){
            e.printStackTrace();
        }

        return 0;
    }
}
