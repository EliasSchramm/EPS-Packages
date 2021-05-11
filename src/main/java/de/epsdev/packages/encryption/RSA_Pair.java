package de.epsdev.packages.encryption;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSA_Pair {

    private KeyPair keys;

    public RSA_Pair(EncryptionMode mode){
        this.keys = generateKeyPair(mode);
    }

    private KeyPair generateKeyPair(EncryptionMode mode) {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(EncryptionMode.getBitValue(mode), new SecureRandom());
            KeyPair pair = generator.generateKeyPair();

            return pair;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encrypt(String plainText, PublicKey publicKey)  {
        try {
            Cipher encryptCipher = Cipher.getInstance("RSA");
            encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

            System.out.println(plainText);

            byte[] cipherText = encryptCipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));


            return Base64.getEncoder().encodeToString(cipherText);
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public static String decrypt(String cipherText, PrivateKey privateKey){
        try {
            byte[] bytes = Base64.getMimeDecoder().decode(cipherText);

            Cipher decriptCipher = Cipher.getInstance("RSA");
            decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);

            return new String(decriptCipher.doFinal(bytes), StandardCharsets.UTF_8);
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public PrivateKey getPrivateKey(){
        return this.keys.getPrivate();
    }

    public PublicKey getPublicKey(){
        return this.keys.getPublic();
    }

    public static String publicToString(PublicKey key){
        try {
            KeyFactory fact = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec spec;
            spec = fact.getKeySpec(key, X509EncodedKeySpec.class);
            return Base64.getMimeEncoder().encodeToString(spec.getEncoded());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public static PublicKey stringToPublic(String s){
        try {
            byte[] buffer = Base64.getMimeDecoder().decode(s);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return keyFactory.generatePublic(keySpec);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

}
