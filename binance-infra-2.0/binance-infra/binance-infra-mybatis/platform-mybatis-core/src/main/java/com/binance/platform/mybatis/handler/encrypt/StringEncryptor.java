package com.binance.platform.mybatis.handler.encrypt;

import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

public class StringEncryptor {

    public static final Logger logger = LoggerFactory.getLogger(StringEncryptor.class);
    public static ConfigurableEnvironment environment;
    private static final String MYBATIS_ENCRYPT_PASSWORD = "mybatis.encrypt.password";
    private static final String MYBATIS_ENCRYPT_SALT = "mybatis.encrypt.salt";

    private static class StringEncryptorHolder {

        private static StringEncryptor stringEncryptor;

        private static void init() {
            synchronized (StringEncryptorHolder.class) {
                if (environment.getProperty(MYBATIS_ENCRYPT_PASSWORD) == null
                    || environment.getProperty(MYBATIS_ENCRYPT_SALT) == null) {
                    logger.error(
                        "Cant not find StringEncryptor. If you want to use EncryptedColumn, you must set you own password and salt.");
                    logger.error("For example: mybatis.encrypt.password=yourpassword mybatis.encrypt.salt=yoursalt");
                    throw new RuntimeException("Cant not find StringEncryptor.");
                }
                StringEncryptorHolder.stringEncryptor = new StringEncryptor(
                    environment.getProperty(MYBATIS_ENCRYPT_PASSWORD), environment.getProperty(MYBATIS_ENCRYPT_SALT));

            }
        }

        public static StringEncryptor getStringEncryptor() {
            if (stringEncryptor == null) {
                init();
            }
            return stringEncryptor;
        }
    }

    public static StringEncryptor getStringEncryptor() {
        return StringEncryptorHolder.getStringEncryptor();
    }

    private final TextEncryptor encryptor;

    public StringEncryptor(String password, String salt) {
        encryptor = Encryptors.delux(password, new String(Hex.encode(salt.getBytes(Charset.forName("utf-8")))));
    }

    public String decrypt(String encryptedText) {
        return encryptor.decrypt(encryptedText);
    }

    public String encrypt(String text) {
        return encryptor.encrypt(text);
    }
}
