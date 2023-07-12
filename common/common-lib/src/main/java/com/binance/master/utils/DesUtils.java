package com.binance.master.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

/**
 *  不推荐DES加密，请使用 EncryptionUtils。
 *  （参考：http://172.18.3.181:9000/organizations/default-organization/rules?open=squid%3AS2278&rule_key=squid%3AS2278）
 */
@Deprecated
public class DesUtils {
    private static final transient Logger logger = LogManager.getLogger(DesUtils.class);

    private static String strDefaultKey = "tianjian_xike.abc#1234";// 字符串默认键值
    private Cipher encryptCipher = null;// 加密工具
    private Cipher decryptCipher = null;// 解密工具

    /**
     * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[] hexStr2ByteArray(String
     * strIn) 互为可逆的转换过程
     *
     * @param arrB 需要转换的byte数组
     * @return 转换后的字符串
     */
    public static String byteArray2HexStr(byte[] arrB) {
        int iLen = arrB.length;
        StringBuilder sb = new StringBuilder(iLen * 2);// 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            while (intTmp < 0) {// 把负数转换为正数
                intTmp = intTmp + 256;
            }
            if (intTmp < 16) {// 小于0F的数需要在前面补0
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    /**
     * 将表示16进制值的字符串转换为byte数组， 和public static String byteArray2HexStr(byte[] arrB) 互为可逆的转换过程
     *
     * @param strIn 需要转换的字符串
     * @return 转换后的byte数组
     */
    public static byte[] hexStr2ByteArray(String strIn) {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;
        byte[] arrOut = new byte[iLen / 2];// 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    /**
     * 默认构造方法，使用默认密钥
     *
     * @throws Exception
     */
    public DesUtils() {
        this(strDefaultKey);
    }

    /**
     * DES字符串加密 指定密钥构造方法
     *
     * @param strKey 指定的密钥
     * @throws Exception
     */
    public DesUtils(String strKey) {
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom random = new SecureRandom();
            // 创建一个DESKeySpec对象
            DESKeySpec desKey = new DESKeySpec(getKey(strKey.getBytes()));
            // 创建一个密匙工厂
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // 将DESKeySpec对象转换成SecretKey对象
            SecretKey securekey = keyFactory.generateSecret(desKey);
            // Cipher对象实际完成解密操作
            encryptCipher = Cipher.getInstance("DES");
            encryptCipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            decryptCipher = Cipher.getInstance("DES");
            decryptCipher.init(Cipher.DECRYPT_MODE, securekey, random);
        } catch (Exception e) {
            logger.error("Cipher初始化异常", e);
        }
    }

    /**
     * 加密字节数组
     *
     * @param arrB 需加密的字节数组
     * @return 加密后的字节数组
     * @throws Exception
     */
    public byte[] encrypt(byte[] arrB) {
        byte[] encrypt = null;
        try {
            encrypt = encryptCipher.doFinal(arrB);
        } catch (Exception e) {
            logger.error("", e);
        }
        return encrypt;
    }

    /**
     * 加密字符串
     *
     * @param strIn 需加密的字符串
     * @return 加密后的字符串
     * @throws Exception
     */
    public String encrypt(String strIn) {
        return byteArray2HexStr(encrypt(strIn.getBytes()));
    }

    /**
     * 解密字节数组
     *
     * @param arrB 需解密的字节数组
     * @return 解密后的字节数组
     * @throws Exception
     */
    public byte[] decrypt(byte[] arrB) {
        byte[] decrypt = null;
        try {
            decrypt = decryptCipher.doFinal(arrB);
        } catch (Exception e) {
            logger.error("", e);
        }
        return decrypt;
    }

    /**
     * 解密字符串
     *
     * @param strIn 需解密的字符串
     * @return 解密后的字符串
     * @throws Exception
     */
    public String decrypt(String strIn) {
        return new String(decrypt(hexStr2ByteArray(strIn)));
    }

    /**
     * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位
     *
     * @param arrBTmp 构成该字符串的字节数组
     * @return 生成的密钥
     * @throws Exception
     */
    private byte[] getKey(byte[] arrBTmp) throws Exception {
        byte[] arrB = new byte[8];// 创建一个空的8位字节数组（默认值为0）
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {// 将原始字节数组转换为8位
            arrB[i] = arrBTmp[i];
        }
        return arrB;
    }
}
