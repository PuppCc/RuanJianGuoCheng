package com.easyse.easyse_simple.utils.encryption;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
/**
 * @author: zky
 * @date: 2022/10/18
 * @description:
 */
@Slf4j
public class AESUtil {
    private static String Algorithm = "AES";
    private static String AlgorithmProvider = "AES/CBC/PKCS5Padding"; //算法/模式/补码方式

    private static String keystr = "0123456789ABCDEF";
    private static String ivstr = "0123456789101112";

    public static byte[] generatorKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(Algorithm);
        keyGenerator.init(256);//默认128，获得无政策权限后可为192或256
        SecretKey secretKey = keyGenerator.generateKey();
        log.info(byteToHexString(secretKey.getEncoded()));
        return secretKey.getEncoded();
    }

    public static IvParameterSpec getIv(String ivstr) throws UnsupportedEncodingException {
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivstr.getBytes("utf-8"));
        return ivParameterSpec;
    }

    public static String encrypt(String src, String keystr, IvParameterSpec iv) throws
            NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            IllegalBlockSizeException,
            BadPaddingException,
            UnsupportedEncodingException,
            InvalidAlgorithmParameterException {
        byte[] key = keystr.getBytes("utf-8");
        SecretKey secretKey = new SecretKeySpec(key, Algorithm);
        IvParameterSpec ivParameterSpec = iv;
        Cipher cipher = Cipher.getInstance(AlgorithmProvider);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
        byte[] cipherBytes = cipher.doFinal(src.getBytes(Charset.forName("utf-8")));
        return byteToHexString(cipherBytes);
    }

    public static String decrypt(String src, String keystr, IvParameterSpec iv) throws Exception {
        byte[] key = keystr.getBytes("utf-8");
        SecretKey secretKey = new SecretKeySpec(key, Algorithm);

        IvParameterSpec ivParameterSpec = iv;
        Cipher cipher = Cipher.getInstance(AlgorithmProvider);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        byte[] hexBytes = hexStringToBytes(src);
        byte[] plainBytes = cipher.doFinal(hexBytes);
        return new String(plainBytes, "utf-8");
    }

    /**
     * 将byte转换为16进制字符串
     *
     * @param src
     * @return
     */
    public static String byteToHexString(byte[] src) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xff;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                sb.append("0");
            }
            sb.append(hv);
        }
        return sb.toString();
    }

    /**
     * 将16进制字符串装换为byte数组
     *
     * @param hexString
     * @return
     */
    public static byte[] hexStringToBytes(String hexString) {
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] b = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            b[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return b;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        try {
            // 密钥必须是16的倍数
//            String keystr = "0123456789ABCDEF";
//            String ivstr = "0123456789101112";
            String src = "Hello World";

            System.out.println("密钥：" + keystr);
            System.out.println("偏移量：" + ivstr);
            System.out.println("原字符串：" + src);
            IvParameterSpec iv = getIv(ivstr);
            String enc = encrypt(src, keystr, iv);
            System.out.println("加密：" + enc);

            String dec = decrypt(enc, keystr, iv);
            String dec1 = decrypt("49ecfffedd1cf9d5c2e3726c42805677", keystr, iv);
            System.out.println("解密：" + dec);
            System.out.println("解密：" + dec1);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        generatorKey();

    }
}
