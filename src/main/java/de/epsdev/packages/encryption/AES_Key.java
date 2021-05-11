package de.epsdev.packages.encryption;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

import java.util.Base64;

public class AES_Key {
    private final SecretKey key;

    public AES_Key(){
        SecretKey k = null;

        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(128);
            k = generator.generateKey();
        }catch (Exception e){
            e.printStackTrace();
        }

        this.key = k;
    }

    public AES_Key(String key){
        byte[] decodedKey = Base64.getDecoder().decode(key);
        this.key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    public String toString(){
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public String encrypt(String plainText){
        try {
            Cipher encryptCipher = Cipher.getInstance("AES");
            encryptCipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] cipherText = encryptCipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(cipherText);
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public String decrypt(String base64){
        try {
            byte[] bytes = Base64.getMimeDecoder().decode(base64);

            Cipher decriptCipher = Cipher.getInstance("AES");
            decriptCipher.init(Cipher.DECRYPT_MODE, key);

            return new String(decriptCipher.doFinal(bytes), StandardCharsets.UTF_8);
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }
}
