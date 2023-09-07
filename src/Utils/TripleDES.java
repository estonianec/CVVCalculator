package Utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.SecretKey;

public class TripleDES {

    public static String encrypt(String data, String key) throws Exception {
        if (key == null || key.length() != 48) {
            throw new IllegalArgumentException("Ключ должен иметь длину 24 байта для Triple DES.");
        }

        byte[] keyBytes = ByteHexUtil.hexToByte(key);
        DESedeKeySpec desedeKeySpec = new DESedeKeySpec(keyBytes);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        SecretKey secretKey = keyFactory.generateSecret(desedeKeySpec);

        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal(ByteHexUtil.hexToByte(data));

        return ByteHexUtil.byteToHex(encryptedData);
    }
}
