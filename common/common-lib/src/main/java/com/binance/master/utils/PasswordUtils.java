package com.binance.master.utils;

import com.binance.master.utils.security.AESEncryptUtil;
import com.binance.master.utils.security.EncryptionUtils;
import com.binance.master.utils.security.HashUtil;

import java.security.NoSuchAlgorithmException;

public class PasswordUtils {


    /**
     * deprecate this method because it's not safe per the request from Security team.
     *
     * @param password
     * @param salt
     * @param cipherCode
     * @return
     * @throws NoSuchAlgorithmException
     */
    @Deprecated
    public static String encode(String password, String salt, String cipherCode) throws NoSuchAlgorithmException {
        String hash = EncryptionUtils.sha(password + salt);
        DesUtils des = new DesUtils(cipherCode);
        String secStr = des.encrypt(hash);
        return secStr;
    }

    /**
     * safe encode method.
     *
     * @param password
     * @param salt
     * @param cipherCode
     * @return
     */
    public static String encode2(String password, String salt, String cipherCode) {
        byte[] hashData = HashUtil.hashWithPBKDF2(password + salt, false);
        byte[] secData = AESEncryptUtil.encrypt(hashData, cipherCode);
        return new String(secData);
    }
}
