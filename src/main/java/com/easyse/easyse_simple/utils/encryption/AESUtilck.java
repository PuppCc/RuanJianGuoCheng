package com.easyse.easyse_simple.utils.encryption;

import com.qiniu.util.Hex;
import io.netty.handler.codec.DecoderException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

/**
 * 每次启动会生成不同的密钥，用于接口数据加密
 */
public class AESUtilck {

    /**
     * 编码格式
     */
    public static final String CHARSET = "UTF-8";

    /**
     * 加密算法
     */
    public static final String AES_ALGORITHM = "AES";

    public static final String IV = "18982671962ABCDE";

    private static Boolean initialized = true;

    /**
     * 生成密钥
     *
     * @return
     */
    public static String generateKey() {
        initialize();
        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            //2.生成一个128位的随机源
//            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
//            secureRandom.setSeed(RANDOM_KEY.getBytes());
            keygen.init(128);
            //3.产生原始对称密钥
            SecretKey secretKey = keygen.generateKey();
            //4.获得原始对称密钥的字节数组
            byte[] byteKey = secretKey.getEncoded();
            //5.返回密钥
//            log.info("原始密钥：{}", Hex.decodeHex(Hex.encodeHexString(byteKey).toCharArray()));
            return Hex.encodeHexString(byteKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //如果有错就返加nulll
        return null;
    }

    /**
     * 加密
     *
     * @param thisKey 密钥
     * @param data    数据
     * @return 加密后的数据
     */
    public static String encode(String thisKey, String data) {
        try {
            //1.转换KEY
            Key key = new SecretKeySpec(Hex.decodeHex(thisKey.toCharArray()), AES_ALGORITHM);
            //2.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
//            IvParameterSpec iv = new IvParameterSpec(IV.getBytes());
            //3.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
//            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //4.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
            byte[] byte_encode = data.getBytes(CHARSET);
            //5.根据密码器的初始化方式--加密：将数据加密
            byte[] result = cipher.doFinal(byte_encode);
            //6.将字符串返回
            return Hex.encodeHexString(result);
        } catch (DecoderException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException | Hex.HexDecodeException e) {
            e.printStackTrace();
        }

        //如果有错就返加nulll
        return null;
    }

    /**
     * 解密
     *
     * @param thisKey 密钥
     * @param data    加密的数据
     * @return 解密后的数据
     */
    public static String decode(String thisKey, String data) {
        initialize();
        try {
            //1.转换KEY
            Key key = new SecretKeySpec(Hex.decodeHex(thisKey.toCharArray()), AES_ALGORITHM);
            //2.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance("AES");
            //3.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.DECRYPT_MODE, key);
            //4.将加密并编码后的内容解码成字节数组
            byte[] byteContent = Hex.decodeHex(data.toCharArray());
            byte[] byteDecode;
            try {
                //5.解密
//                System.out.println("input-Length::" + Arrays.toString(byteContent) + "  " + byteContent.length);
                byteDecode = cipher.doFinal(byteContent);
            } catch (Exception e) {
                throw new Exception("解密失败！", e);
            }
            //6.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
            return new String(byteDecode, CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //如果有错就返加nulll
        return null;
    }

    private static void initialize() {
        if (initialized) {
            return;
        }
        Security.addProvider(new BouncyCastleProvider());
        initialized = true;

    }

    public static void main(String[] args) {
        String key = generateKey();
//        key = "cdf1007ccc8808dbb82968b41cdf66aa";
        System.out.println("生成的密钥为：" + key);
//        String pubKey = "6aa7066f738828f333b1bb84b62606c6";
//        String priKey = "6aa7066f738828f333b1bb84b62606c6";
        String data = "123123";
        String encodeData = encode(key, data);
        System.out.println("加密后的数据为：" + encodeData);
//        encodeData = "d05a72ed4a832b97c3388743c462eb1e";
        String decodeData = decode(key, encodeData);
        System.out.println("解密后的数据为：" + decodeData);
    }

}