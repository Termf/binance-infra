package com.binance.master.signature.rsa;

import java.io.IOException;

import org.junit.Assert;

public class RSASignatureVerifierTest {

    private String pubKey = "-----BEGIN PUBLIC KEY-----\n MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAs33jv7ty/TMB0a3QFLC7 1c1mQFkRwBu8iLINq+MLzVG01fL+C/BbyLh6Cqs+yrDdgJZE5C7qQ2ILl508gHj9 kaqusR0ufgwZHCJDGaWzxYnvqRMdkuljMnsxrPHI6eEHMyB/flge6b8EOWZkV9K5 SVP+8ynXtxFcpLgzC9Fei2om5BEnVEUfJAeImu6uSLEFCpboaKXynLCK4JmR1fzG Owg9a6mBqEVg/pIrwDqzzsmiozg1bJNimzwTwLO8JIsRifjsSzy8SmDAf7end05l c9H4UBa8BfAqWi2cqiEukwSu+SvFAsBdqp2SOy/TzrK3ZP5Lm47jaAMckAvM1d4U aBvaDJg2sQdcFQtkMFR6tg7NIZduq1BroLW5liz6D8u2dSXTOeFSfux0BOHn+XAA BF7Tu5HQdczwJORWs/QVUFcPheFuEEYgkCtK1HlWdcwsyZAoEoNkx2m/JvKeMX8i 0bL0cIhGUC5WiOtrOQbZuKrLc9HEU2vvySVnva9Ru5o8BFrl4Ci1KjbtZVUj7Hjh NciVeU6lmBRadZpO021uGVJDpv/NZcL1VGWTWatOC9eYzw9Caq0xf2SBP8224/bt zlYaWfc3cMnGWwLaXcQJE3XubRuPEM+aIcTsUeYdDa3Jj4/q8hPxKBJ0tus79mtE Wsqoq/X1Az7zS8n9gFhkV/8CAwEAAQ== \n-----END PUBLIC KEY-----";

    @org.junit.Test
    public void verifySignature() {
        try {
            RSASignatureVerifier verifier = new RSASignatureVerifier(pubKey);
            Assert.assertNotNull(verifier);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }
}