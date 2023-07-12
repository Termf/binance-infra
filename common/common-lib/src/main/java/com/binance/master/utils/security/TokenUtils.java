package com.binance.master.utils.security;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RegExUtils;
import org.springframework.util.Assert;

import com.binance.master.utils.DateUtils;
import com.binance.master.utils.LogUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * token辅助类
 */
public class TokenUtils {

    /**
     * 创建一个 token
     * 
     * @param subject 必传 该值可以是json也可以是一个值 使用方决定传什么
     * @param expireTime 必须 过期时间
     * @param secret 必须 sign密钥
     * @return token
     */
    public static String createJWT(String subject, Date expireTime, String secret) {
        Assert.notNull(subject, "subject is not null");
        Assert.notNull(expireTime, "expireTime is not null");
        Assert.notNull(secret, "secret is not null");
        secret = HashUtil.sha256Hex(secret);
        return Jwts.builder().setSubject(subject).setExpiration(expireTime).compressWith(CompressionCodecs.DEFLATE)
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    /**
     * token 解密
     * 
     * @param token 必须jwt秘钥
     * @param secret 必须安全秘钥
     * @return
     */
    public static String parseJWTSubject(String token, String secret) {
        Assert.notNull(token, "token is not null");
        Assert.notNull(secret, "secret is not null");
        secret = HashUtil.sha256Hex(secret);
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        String subject = claims.getSubject();
        return subject;
    }

    private static String EMAIL_DATE_FORMAT = "yyyyMMddHHmmss";

    /**
     * generate token with uuid and PBKDF2 hash algorithm.
     *
     * @return
     */
    public static String emailRedisToken() {
        StringBuffer sb = new StringBuffer(DateUtils.formatterUTC(DateUtils.getNewUTCDate(), EMAIL_DATE_FORMAT));
        sb.append(Hex.encodeHexString(HashUtil.hashWithPBKDF2(uuid(), true)));
        return sb.toString();
    }

    private static String uuid() {
        return RegExUtils.removeAll(UUID.randomUUID().toString(), "-");
    }

    public static boolean isEmailVerifyCodeExpire(String emailToken, int time) {
        try {
            String timeStr = emailToken.substring(0, EMAIL_DATE_FORMAT.length());
            Date dateTime = DateUtils.formatterUTC(timeStr, EMAIL_DATE_FORMAT);// UTC
            long historyTime = DateUtils.add(dateTime, Calendar.MINUTE, time).getTime();
            long newTime = DateUtils.getNewUTCDate().getTime();
            LogUtils.info("historyTime:{},newTime:{}", historyTime, newTime);
            if (historyTime > newTime) {
                return true;
            }
        } catch (Exception e) {
            LogUtils.error("isEmailVerifyCodeExpire:{}", e);
            return false;
        }
        return false;
    }

}
