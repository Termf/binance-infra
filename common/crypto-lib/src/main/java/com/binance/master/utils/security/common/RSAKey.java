package com.binance.master.utils.security.common;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RSAKey {
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;
}
