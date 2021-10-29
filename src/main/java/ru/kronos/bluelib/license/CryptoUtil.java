package ru.kronos.bluelib.license;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class CryptoUtil {
	public static final String ALGORITHM = "RSA";
    public static final String PRIVATE_KEY_FILE = "private.key";
    public static final String PUBLIC_KEY_FILE = "public.key";
 
    public static void generateKey() {
        try {
            final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
            keyGen.initialize(2048, new SecureRandom());
            final KeyPair key = keyGen.generateKeyPair();
            
            File privateKeyFile = new File(PRIVATE_KEY_FILE);
            File publicKeyFile = new File(PUBLIC_KEY_FILE);
            
            if (privateKeyFile.getParentFile() != null) {
                privateKeyFile.getParentFile().mkdirs();
            }
            privateKeyFile.createNewFile();
            
            if (publicKeyFile.getParentFile() != null) {
                publicKeyFile.getParentFile().mkdirs();
            }
            publicKeyFile.createNewFile();
 
            BufferedWriter pubOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(publicKeyFile)));
            pubOut.write(byte2Hex(key.getPublic().getEncoded()));
            pubOut.flush();
            pubOut.close();
 
            BufferedWriter privOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(privateKeyFile)));
            privOut.write(byte2Hex(key.getPrivate().getEncoded()));
            privOut.flush();
            privOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
 
    }
 
    public static byte[] encrypt(String text, Key key) {
        byte[] cipherText = null;

        try {
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            cipherText = cipher.doFinal(text.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cipherText;
    }
 
    public static String decrypt(byte[] text, Key key) {
        byte[] dectyptedText = null;

        try {
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            dectyptedText = cipher.doFinal(text);
 
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        assert dectyptedText != null;
        return new String(dectyptedText);
    }
 
    @SuppressWarnings("resource")
	private static byte[] fileToKey(String file) throws IOException {
        BufferedReader pubIn = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        StringBuilder sb = new StringBuilder();
        String tmp;
        do {
            tmp = pubIn.readLine();
            if (tmp != null) sb.append(tmp);
        } while (tmp != null);
        return hex2Byte(sb.toString());
    }
 
    public static PublicKey restorePublic() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(fileToKey(PUBLIC_KEY_FILE));
        return keyFactory.generatePublic(publicKeySpec);
    }
 
    public static PrivateKey restorePrivate() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(fileToKey(PRIVATE_KEY_FILE));
        return keyFactory.generatePrivate(privateKeySpec);
    }
 
    
 
    public static String byte2Hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (byte value : b) {
            stmp = Integer.toHexString(value & 0xff);
            if (stmp.length() == 1)
                hs.append("0").append(stmp);
            else
                hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }
 
    public static byte hex2Byte(char a1, char a2) {
        int k;
        if (a1 >= '0' && a1 <= '9') k = a1 - 48;
        else if (a1 >= 'a' && a1 <= 'f') k = (a1 - 97) + 10;
        else if (a1 >= 'A' && a1 <= 'F') k = (a1 - 65) + 10;
        else k = 0;
        k <<= 4;
        if (a2 >= '0' && a2 <= '9') k += a2 - 48;
        else if (a2 >= 'a' && a2 <= 'f') k += (a2 - 97) + 10;
        else if (a2 >= 'A' && a2 <= 'F') k += (a2 - 65) + 10;
        else k += 0;
        return (byte) (k & 0xff);
    }
 
    public static byte[] hex2Byte(String str) {
        int len = str.length();
        if (len % 2 != 0) return null;
        byte[] r = new byte[len / 2];
        int k = 0;
        for (int i = 0; i < str.length() - 1; i += 2) {
            r[k] = hex2Byte(str.charAt(i), str.charAt(i + 1));
            k++;
        }
        return r;
    }
}
