package de.epsdev.packages;

import de.epsdev.packages.encryption.AES_Key;
import de.epsdev.packages.encryption.RSA_Pair;
import de.epsdev.packages.packages.Package;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.PublicKey;

public class HandshakeSequence {
    public static void serverSide(Socket s){
        try {
            OutputStream outToServer = s.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            if(Package.KEYS != null) out.writeUTF(RSA_Pair.publicToString(Package.KEYS.getPublicKey()));
            else{
                out.writeUTF("null");
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

    public static void clientSide(Socket s){
        try {
            DataInputStream in;
            in = new DataInputStream(s.getInputStream());

            String data = in.readUTF();
            if(data.equalsIgnoreCase("null")) return;

            PublicKey rsa_key = RSA_Pair.stringToPublic(data);
            AES_Key key = new AES_Key();

            OutputStream outToServer = s.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            out.writeUTF(RSA_Pair.encrypt(key.toString(), rsa_key));

            Package.CLIENT_KEYS.put(s.toString(), key);
            Package.KEYS = new RSA_Pair();

        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
