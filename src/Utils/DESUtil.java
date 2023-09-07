package Utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DESUtil {
    public static String encrypt(String data, String hexKey) throws Exception {
        byte[] keyBytes = ByteHexUtil.hexToByte(hexKey);

        if (keyBytes.length != 8) {
            throw new IllegalArgumentException("Key must be 8 bytes long when decoded from hex");
        }

        if (data.length() != 16) {
            throw new IllegalArgumentException("Data must be 16 hex characters long (representing 8 bytes)");
        }
        byte[] inputData = ByteHexUtil.hexToByte(data);

        DESKeySpec desKeySpec = new DESKeySpec(keyBytes);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal(inputData);

        return ByteHexUtil.byteToHex(encryptedData);
    }
}

