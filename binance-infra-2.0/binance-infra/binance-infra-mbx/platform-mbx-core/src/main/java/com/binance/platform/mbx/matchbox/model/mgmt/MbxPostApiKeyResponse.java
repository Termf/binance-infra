package com.binance.platform.mbx.matchbox.model.mgmt;

/**
 * @author Li Fenggang
 * Date: 2020/7/19 9:00 上午
 */
public class MbxPostApiKeyResponse {

    /**
     * apiKey : 8XKiLkdvVKMR5kWSjAbihR64VJFB907uB8kvwINgQx7BheCPrkencmnUs315sgpT
     * secretKey : AyVjcL1s0e5QsJ4CBKAX4MdWJIMv4u22ptfKYVik78GG5ruGSxO8RewjOWbR9VIJ
     * keyId : 56
     * type : HMAC_SHA256
     */

    private String apiKey;
    private String secretKey;
    private Long keyId;
    private String type;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Long getKeyId() {
        return keyId;
    }

    public void setKeyId(Long keyId) {
        this.keyId = keyId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
