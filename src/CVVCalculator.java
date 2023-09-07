import Utils.ByteHexUtil;
import Utils.DESUtil;
import Utils.TripleDES;

public class CVVCalculator {

    private static final char[] decTable = "0123456789012345".toCharArray();

    private static int hexToByte(char ch) {
        if ('0' <= ch && ch <= '9') return ch - '0';
        if ('A' <= ch && ch <= 'F') return ch - 'A' + 10;
        if ('a' <= ch && ch <= 'f') return ch - 'a' + 10;
        return -1;
    }

    public static void main(String[] args) throws Exception {
        String CVK1 = "F40157F249232FCE";
        String CVK2 = "7CE6C8CB9E8683EC";
        String EXP_YYMM = "2005";
        String SVC = "520";
        String PAN = "4263970000005262";
        String CVV = calculateCVV(CVK1, CVK2, PAN, SVC, EXP_YYMM);
        System.out.println("Expected CVV OUTPUT: 782");
        System.out.println("Generated CVV: " + CVV);
    }

    public static String calculateCVV(final String cvk1, final String cvk2, final String pan, final String svc, final String expiry) throws Exception {
        String input = String.format("%-32s", pan + expiry + svc).replace(' ', '0');
        String data1 = input.substring(0, 16);
        String data2 = input.substring(16);
        String d1 = DESUtil.encrypt(data1, cvk1);
        String d2 = ByteHexUtil.byteToHex(ByteHexUtil.xor(ByteHexUtil.hexToByte(d1), ByteHexUtil.hexToByte(data2)));
        String d3 = TripleDES.encrypt(d2, cvk1 + cvk2 + cvk1);
        return decimalizeDigitsFirst(d3, 3);
    }

    public static String decimalizeDigitsFirst(String data, final int outLen) {
        StringBuilder selected = new StringBuilder(outLen);
        int selectionCounter = 0;
        int len = data.length();
        for (int i = 0; i < len && selectionCounter < outLen; i++) {
            if (hexToByte(data.charAt(i)) < 10) {
                selected.append(data.charAt(i));
                selectionCounter++;
            }
        }
        if (selectionCounter != 2) {
            for (int i = 0; i < len && selectionCounter < outLen; i++) {
                if (hexToByte(data.charAt(i)) > 9) {
                    selected.append(decTable[hexToByte(data.charAt(i))]);
                    selectionCounter++;
                }
            }
        }
        return selected.toString();
    }
}
