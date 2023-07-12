package com.binance.platform.mbx.cloud.model;

import com.binance.master.commons.ToString;
import com.binance.master.constant.Constant;
import com.binance.master.utils.BitUtils;
import com.binance.platform.mbx.constant.AccountCommonConstant;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户转态
 *
 * @author wang-bijie
 */
public class UserStatusEx extends ToString {

    /**
     *
     */
    private static final long serialVersionUID = 745013793286943653L;

    @ApiModelProperty(readOnly = true, notes = "用户是否激活")
    private Boolean isUserActive;

    @ApiModelProperty(readOnly = true, notes = "用户是否禁用")
    private Boolean isUserDisabled;

    @ApiModelProperty(readOnly = true, notes = "用户是否禁用登录")
    private Boolean isUserDisabledLogin;

    @ApiModelProperty(readOnly = true, notes = "用户锁定")
    private Boolean isUserLock;

    @ApiModelProperty(readOnly = true, notes = "特殊用户")
    private Boolean isUserSpecial;

    @ApiModelProperty(readOnly = true, notes = "种子用户")
    private Boolean isUserSend;

    @ApiModelProperty(readOnly = true, notes = "协议确定")
    private Boolean isUserProtocol;

    @ApiModelProperty(readOnly = true, notes = "用户手机验证")
    private Boolean isUserMobile;

    @ApiModelProperty(readOnly = true, notes = "用户强制修改密码")
    private Boolean isUserForcedPassword;

    @ApiModelProperty(readOnly = true, notes = "是否是子账号")
    private Boolean isSubUser;

    @ApiModelProperty(readOnly = true, notes = "子账户是否被母账户启用")
    private Boolean isSubUserEnabled;

    @ApiModelProperty(readOnly = true, notes = "是否是资管子账户")
    private Boolean isAssetSubUser;

    @ApiModelProperty(readOnly = true, notes = "资管子账户是否被母账户启用")
    private Boolean isAssetSubUserEnabled;

    @ApiModelProperty(readOnly = true, notes = "申购是否禁用")
    private Boolean isUserPurchase;

    @ApiModelProperty(readOnly = true, notes = "交易是否禁用")
    private Boolean isUserTrade;

    @ApiModelProperty(readOnly = true, notes = "子母账户功能是否开启")
    private Boolean isSubUserFunctionEnabled;

    @ApiModelProperty(readOnly = true, notes = "app交易是否禁用")
    private Boolean isUserTradeApp;

    @ApiModelProperty(readOnly = true, notes = "api交易是否禁用")
    private Boolean isUserTradeApi;

    @ApiModelProperty(readOnly = true, notes = "BNB手续费开关是否开启")
    private Boolean isUserFee;

    @ApiModelProperty(readOnly = true, notes = "是否开启谷歌2次验证")
    private Boolean isUserGoogle;

    @ApiModelProperty(readOnly = true, notes = "是否开启提币白名单")
    private Boolean isWithdrawWhite;

    @ApiModelProperty(readOnly = true, notes = "是否删除用户")
    private Boolean isUserDelete;

    @ApiModelProperty(readOnly = true, notes = "用户是否实名认证")
    private Boolean isUserCertification;

    @ApiModelProperty(readOnly = true, notes = "用户实名认证类型。0：个人，1：企业")
    private Boolean userCertificationType;


    @ApiModelProperty(readOnly = true, notes = "是否是margin_user")
    private Boolean isMarginUser;

    @ApiModelProperty(readOnly = true, notes = "是否拥有margin账户")
    private Boolean isExistMarginAccount;

    @ApiModelProperty(readOnly = true, notes = "是否是fiat_user")
    private Boolean isFiatUser;

    @ApiModelProperty(readOnly = true, notes = "是否拥有主站fiat账户")
    private Boolean isExistFiatAccount;

    @ApiModelProperty(readOnly = true, notes = "是否是future_user")
    private Boolean isFutureUser;

    @ApiModelProperty(readOnly = true, notes = "是否拥有future账户")
    private Boolean isExistFutureAccount;
    @ApiModelProperty(readOnly = true, notes = "是否签署开通法币交易协议")
    private Boolean isFiatProtocolConfirm;

    @ApiModelProperty(readOnly = true, notes = "是否禁止期货内部划转")
    private Boolean disableFutureInternalTransfer;

    @ApiModelProperty(readOnly = true, notes = "broker子母账户功能是否开启")
    private Boolean isBrokerSubUserFunctionEnabled;

    @ApiModelProperty(readOnly = true, notes = "是否是broker子账号")
    private Boolean isBrokerSubUser;

    @ApiModelProperty(readOnly = true, notes = "broker子账户是否被broker母账户启用")
    private Boolean isBrokerSubUserEnabled;

    @ApiModelProperty(readOnly = true, notes = "是否开启快速提币")
    private Boolean userFastWithdrawEnabled;

    @ApiModelProperty(value = "是否提交过返佣设置的表格")
    private Boolean isReferralSettingSubmitted;

    @ApiModelProperty(value = "是否禁止broker划转")
    private Boolean isForbiddenBrokerTrasnfer;



    @ApiModelProperty(readOnly = true, notes = "是否是矿池账户")
    private Boolean isMiningUser;

    @ApiModelProperty(readOnly = true, notes = "是否拥有矿池账户")
    private Boolean isExistMiningAccount;


    @ApiModelProperty(value = "是否开启资金密码")
    private Boolean isUserFundPassword;

    @ApiModelProperty(readOnly = true, notes = "是否签署leverage token风险协议")
    private Boolean isSignedLVTRiskAgreement;

    @ApiModelProperty(readOnly = true, notes = "是否是isolated_margin_user")
    private Boolean isIsolatedMarginUser;

    @ApiModelProperty(readOnly = true, notes = "是否拥有isolated_margin账户")
    private Boolean isExistIsolatedMarginAccount;

    @ApiModelProperty(readOnly = true, notes = "是否是手机号注册的用户")
    private Boolean isMobileUser;

    @ApiModelProperty(readOnly = true, notes = "是否没有绑定邮箱")
    private Boolean isUserNotBindEmail;

    @ApiModelProperty(readOnly = true, notes = "是否是一键注册用户")
    private Boolean isOneButtonRegisterUser;

    @ApiModelProperty(readOnly = true, notes = "是否是CardUser")
    private Boolean isCardUser;

    @ApiModelProperty(readOnly = true, notes = "是否拥有Card账户")
    private Boolean isExistCardAccount;

    @ApiModelProperty(readOnly = true, notes = "是否是WaasUser")
    private Boolean isWaasUser;

    public UserStatusEx() {
    }

    public UserStatusEx(Long status) {
        super();
        this.isUserActive = BitUtils.isTrue(status, Constant.USER_ACTIVE);
        this.isUserDisabled = BitUtils.isTrue(status, Constant.USER_DISABLED);
        this.isUserLock = BitUtils.isTrue(status, Constant.USER_LOCK);
        this.isUserSpecial = BitUtils.isTrue(status, Constant.USER_SPECIAL);
        this.isUserSend = BitUtils.isTrue(status, Constant.USER_SEND);
        this.isUserProtocol = BitUtils.isTrue(status, Constant.USER_PROTOCOL);
        this.isUserMobile = BitUtils.isTrue(status, Constant.USER_MOBILE);
        this.isUserForcedPassword = BitUtils.isTrue(status, Constant.USER_FORCED_PASSWORD);
        this.isSubUser = BitUtils.isTrue(status, Constant.USER_IS_SUBUSER);
        this.isSubUserEnabled = BitUtils.isTrue(status, Constant.USER_IS_SUB_USER_ENABLED);

        this.isAssetSubUser = BitUtils.isTrue(status, Constant.USER_IS_ASSET_SUBUSER);
        this.isAssetSubUserEnabled = BitUtils.isTrue(status, Constant.USER_IS_ASSET_SUB_USER_ENABLED);

        this.isUserPurchase = BitUtils.isTrue(status, Constant.USER_PURCHASE);
        this.isUserTrade = BitUtils.isTrue(status, Constant.USER_TRADE);
        this.isSubUserFunctionEnabled = BitUtils.isTrue(status, Constant.USER_IS_SUBUSER_FUNCTION_ENABLED);
        this.isUserTradeApp = BitUtils.isTrue(status, Constant.USER_TRADE_APP);
        this.isUserTradeApi = BitUtils.isTrue(status, Constant.USER_TRADE_API);
        this.isUserFee = BitUtils.isTrue(status, Constant.USER_FEE);
        this.isUserGoogle = BitUtils.isTrue(status, Constant.USER_GOOGLE);
        this.isWithdrawWhite = BitUtils.isTrue(status, Constant.USER_WITHDRAW_WHITE);
        this.isUserDelete = BitUtils.isTrue(status, Constant.USER_DELETE);
        this.isUserCertification = BitUtils.isTrue(status, Constant.USER_CERTIFICATION);
        this.userCertificationType = BitUtils.isTrue(status, Constant.USER_CERTIFICATION_TYPE);
        this.isMarginUser = BitUtils.isTrue(status, Constant.USER_IS_MARGIN_USER);
        this.isExistMarginAccount = BitUtils.isTrue(status, Constant.USER_IS_EXIST_MARGIN_ACCOUNT);
        this.isFiatUser = BitUtils.isTrue(status, Constant.USER_IS_FIAT_USER);
        this.isExistFiatAccount = BitUtils.isTrue(status, Constant.USER_IS_EXIST_FIAT_ACCOUNT);
        this.isUserDisabledLogin = BitUtils.isTrue(status, Constant.USER_LOGIN);
        this.isFutureUser = BitUtils.isTrue(status,  Constant.USER_IS_FUTURE_USER);
        this.isExistFutureAccount = BitUtils.isTrue(status,  Constant.USER_IS_EXIST_FUTURE_ACCOUNT);
        this.disableFutureInternalTransfer = BitUtils.isTrue(status, Constant.DISABLE_FUTURE_INTERNAL_TRANSFER);

        this.isBrokerSubUserFunctionEnabled = BitUtils.isTrue(status, Constant.USER_IS_BROKER_SUBUSER_FUNCTION_ENABLED);
        this.isBrokerSubUser = BitUtils.isTrue(status, Constant.USER_IS_BROKER_SUBUSER);
        this.isBrokerSubUserEnabled = BitUtils.isTrue(status, Constant.USER_IS_BROKER_SUB_USER_ENABLED);
        this.isFiatProtocolConfirm = BitUtils.isTrue(status, Constant.FIAT_PROTOCOL_CONFIRM);
        this.userFastWithdrawEnabled = !BitUtils.isTrue(status, Constant.USER_FAST_WITHDRAW_ENABLED);
        this.isReferralSettingSubmitted = BitUtils.isTrue(status, Constant.USER_TRADE_COMMISSION_ENABLED);

        this.isForbiddenBrokerTrasnfer = BitUtils.isTrue(status, AccountCommonConstant.FORBIDDEN_BROKER_TRANSFER);


        this.isMiningUser = BitUtils.isTrue(status, AccountCommonConstant.USER_IS_MINING_USER);
        this.isCardUser = BitUtils.isTrue(status, AccountCommonConstant.USER_IS_CARD_USER);
        this.isExistMiningAccount = BitUtils.isTrue(status, AccountCommonConstant.USER_IS_EXIST_MINING_ACCOUNT);
        this.isExistCardAccount = BitUtils.isTrue(status, AccountCommonConstant.USER_IS_EXIST_CARD_ACCOUNT);


        this.isUserFundPassword = BitUtils.isTrue(status, AccountCommonConstant.USER_FUND_PASSWORD);

        this.isSignedLVTRiskAgreement = BitUtils.isTrue(status, AccountCommonConstant.SIGNED_LVT_RISK_AGREEMENT);

        this.isIsolatedMarginUser = BitUtils.isTrue(status, AccountCommonConstant.USER_IS_ISOLATED_MARGIN_USER);
        this.isExistIsolatedMarginAccount = BitUtils.isTrue(status, AccountCommonConstant.USER_IS_EXIST_ISOLATED_MARGIN_ACCOUNT);

        this.isMobileUser = BitUtils.isTrue(status, AccountCommonConstant.USER_IS_MOBILE_USER);
        this.isUserNotBindEmail = BitUtils.isTrue(status, AccountCommonConstant.USER_NOT_BIND_EMAIL);

        this.isWaasUser = BitUtils.isTrue(status, AccountCommonConstant.USER_IS_WAAS_USER);
        this.isOneButtonRegisterUser = BitUtils.isTrue(status, AccountCommonConstant.ONE_BUTTON_REGISTER_USER);
    }

    public Boolean getIsUserActive() {
        return this.isUserActive;
    }

    public Boolean getIsUserDisabled() {
        return this.isUserDisabled;
    }

    public Boolean getIsUserDisabledLogin() {
        return this.isUserDisabledLogin;
    }

    public Boolean getIsUserLock() {
        return this.isUserLock;
    }

    public Boolean getIsUserSpecial() {
        return this.isUserSpecial;
    }

    public Boolean getIsUserSend() {
        return this.isUserSend;
    }

    public Boolean getIsUserProtocol() {
        return this.isUserProtocol;
    }

    public Boolean getIsUserMobile() {
        return this.isUserMobile;
    }

    public Boolean getIsUserForcedPassword() {
        return this.isUserForcedPassword;
    }

    public Boolean getIsSubUser() {
        return this.isSubUser;
    }

    public Boolean getIsSubUserEnabled() {
        return this.isSubUserEnabled;
    }

    public Boolean getIsAssetSubUser() {
        return this.isAssetSubUser;
    }

    public Boolean getIsAssetSubUserEnabled() {
        return this.isAssetSubUserEnabled;
    }

    public Boolean getIsUserPurchase() {
        return this.isUserPurchase;
    }

    public Boolean getIsUserTrade() {
        return this.isUserTrade;
    }

    public Boolean getIsSubUserFunctionEnabled() {
        return this.isSubUserFunctionEnabled;
    }

    public Boolean getIsUserTradeApp() {
        return this.isUserTradeApp;
    }

    public Boolean getIsUserTradeApi() {
        return this.isUserTradeApi;
    }

    public Boolean getIsUserFee() {
        return this.isUserFee;
    }

    public Boolean getIsUserGoogle() {
        return this.isUserGoogle;
    }

    public Boolean getIsWithdrawWhite() {
        return this.isWithdrawWhite;
    }

    public Boolean getIsUserDelete() {
        return this.isUserDelete;
    }

    public Boolean getIsUserCertification() {
        return this.isUserCertification;
    }

    public Boolean getUserCertificationType() {
        return this.userCertificationType;
    }

    public Boolean getIsMarginUser() {
        return this.isMarginUser;
    }

    public Boolean getIsExistMarginAccount() {
        return this.isExistMarginAccount;
    }

    public Boolean getIsFiatUser() {
        return this.isFiatUser;
    }

    public Boolean getIsExistFiatAccount() {
        return this.isExistFiatAccount;
    }

    public Boolean getIsFutureUser() {
        return this.isFutureUser;
    }

    public Boolean getIsExistFutureAccount() {
        return this.isExistFutureAccount;
    }

    public Boolean getIsFiatProtocolConfirm() {
        return this.isFiatProtocolConfirm;
    }

    public Boolean getDisableFutureInternalTransfer() {
        return this.disableFutureInternalTransfer;
    }

    public Boolean getIsBrokerSubUserFunctionEnabled() {
        return this.isBrokerSubUserFunctionEnabled;
    }

    public Boolean getIsBrokerSubUser() {
        return this.isBrokerSubUser;
    }

    public Boolean getIsBrokerSubUserEnabled() {
        return this.isBrokerSubUserEnabled;
    }

    public Boolean getUserFastWithdrawEnabled() {
        return this.userFastWithdrawEnabled;
    }

    public Boolean getIsReferralSettingSubmitted() {
        return this.isReferralSettingSubmitted;
    }

    public Boolean getIsForbiddenBrokerTrasnfer() {
        return this.isForbiddenBrokerTrasnfer;
    }

    public Boolean getIsMiningUser() {
        return this.isMiningUser;
    }

    public Boolean getIsExistMiningAccount() {
        return this.isExistMiningAccount;
    }

    public Boolean getIsUserFundPassword() {
        return this.isUserFundPassword;
    }

    public Boolean getIsSignedLVTRiskAgreement() {
        return this.isSignedLVTRiskAgreement;
    }

    public Boolean getIsIsolatedMarginUser() {
        return this.isIsolatedMarginUser;
    }

    public Boolean getIsExistIsolatedMarginAccount() {
        return this.isExistIsolatedMarginAccount;
    }

    public Boolean getIsMobileUser() {
        return this.isMobileUser;
    }

    public Boolean getIsUserNotBindEmail() {
        return this.isUserNotBindEmail;
    }

    public void setIsUserActive(final Boolean isUserActive) {
        this.isUserActive = isUserActive;
    }

    public void setIsUserDisabled(final Boolean isUserDisabled) {
        this.isUserDisabled = isUserDisabled;
    }

    public void setIsUserDisabledLogin(final Boolean isUserDisabledLogin) {
        this.isUserDisabledLogin = isUserDisabledLogin;
    }

    public void setIsUserLock(final Boolean isUserLock) {
        this.isUserLock = isUserLock;
    }

    public void setIsUserSpecial(final Boolean isUserSpecial) {
        this.isUserSpecial = isUserSpecial;
    }

    public void setIsUserSend(final Boolean isUserSend) {
        this.isUserSend = isUserSend;
    }

    public void setIsUserProtocol(final Boolean isUserProtocol) {
        this.isUserProtocol = isUserProtocol;
    }

    public void setIsUserMobile(final Boolean isUserMobile) {
        this.isUserMobile = isUserMobile;
    }

    public void setIsUserForcedPassword(final Boolean isUserForcedPassword) {
        this.isUserForcedPassword = isUserForcedPassword;
    }

    public void setIsSubUser(final Boolean isSubUser) {
        this.isSubUser = isSubUser;
    }

    public void setIsSubUserEnabled(final Boolean isSubUserEnabled) {
        this.isSubUserEnabled = isSubUserEnabled;
    }

    public void setIsAssetSubUser(final Boolean isAssetSubUser) {
        this.isAssetSubUser = isAssetSubUser;
    }

    public void setIsAssetSubUserEnabled(final Boolean isAssetSubUserEnabled) {
        this.isAssetSubUserEnabled = isAssetSubUserEnabled;
    }

    public void setIsUserPurchase(final Boolean isUserPurchase) {
        this.isUserPurchase = isUserPurchase;
    }

    public void setIsUserTrade(final Boolean isUserTrade) {
        this.isUserTrade = isUserTrade;
    }

    public void setIsSubUserFunctionEnabled(final Boolean isSubUserFunctionEnabled) {
        this.isSubUserFunctionEnabled = isSubUserFunctionEnabled;
    }

    public void setIsUserTradeApp(final Boolean isUserTradeApp) {
        this.isUserTradeApp = isUserTradeApp;
    }

    public void setIsUserTradeApi(final Boolean isUserTradeApi) {
        this.isUserTradeApi = isUserTradeApi;
    }

    public void setIsUserFee(final Boolean isUserFee) {
        this.isUserFee = isUserFee;
    }

    public void setIsUserGoogle(final Boolean isUserGoogle) {
        this.isUserGoogle = isUserGoogle;
    }

    public void setIsWithdrawWhite(final Boolean isWithdrawWhite) {
        this.isWithdrawWhite = isWithdrawWhite;
    }

    public void setIsUserDelete(final Boolean isUserDelete) {
        this.isUserDelete = isUserDelete;
    }

    public void setIsUserCertification(final Boolean isUserCertification) {
        this.isUserCertification = isUserCertification;
    }

    public void setUserCertificationType(final Boolean userCertificationType) {
        this.userCertificationType = userCertificationType;
    }

    public void setIsMarginUser(final Boolean isMarginUser) {
        this.isMarginUser = isMarginUser;
    }

    public void setIsExistMarginAccount(final Boolean isExistMarginAccount) {
        this.isExistMarginAccount = isExistMarginAccount;
    }

    public void setIsFiatUser(final Boolean isFiatUser) {
        this.isFiatUser = isFiatUser;
    }

    public void setIsExistFiatAccount(final Boolean isExistFiatAccount) {
        this.isExistFiatAccount = isExistFiatAccount;
    }

    public void setIsFutureUser(final Boolean isFutureUser) {
        this.isFutureUser = isFutureUser;
    }

    public void setIsExistFutureAccount(final Boolean isExistFutureAccount) {
        this.isExistFutureAccount = isExistFutureAccount;
    }

    public void setIsFiatProtocolConfirm(final Boolean isFiatProtocolConfirm) {
        this.isFiatProtocolConfirm = isFiatProtocolConfirm;
    }

    public void setDisableFutureInternalTransfer(final Boolean disableFutureInternalTransfer) {
        this.disableFutureInternalTransfer = disableFutureInternalTransfer;
    }

    public void setIsBrokerSubUserFunctionEnabled(final Boolean isBrokerSubUserFunctionEnabled) {
        this.isBrokerSubUserFunctionEnabled = isBrokerSubUserFunctionEnabled;
    }

    public void setIsBrokerSubUser(final Boolean isBrokerSubUser) {
        this.isBrokerSubUser = isBrokerSubUser;
    }

    public void setIsBrokerSubUserEnabled(final Boolean isBrokerSubUserEnabled) {
        this.isBrokerSubUserEnabled = isBrokerSubUserEnabled;
    }

    public void setUserFastWithdrawEnabled(final Boolean userFastWithdrawEnabled) {
        this.userFastWithdrawEnabled = userFastWithdrawEnabled;
    }

    public void setIsReferralSettingSubmitted(final Boolean isReferralSettingSubmitted) {
        this.isReferralSettingSubmitted = isReferralSettingSubmitted;
    }

    public void setIsForbiddenBrokerTrasnfer(final Boolean isForbiddenBrokerTrasnfer) {
        this.isForbiddenBrokerTrasnfer = isForbiddenBrokerTrasnfer;
    }

    public void setIsMiningUser(final Boolean isMiningUser) {
        this.isMiningUser = isMiningUser;
    }

    public void setIsExistMiningAccount(final Boolean isExistMiningAccount) {
        this.isExistMiningAccount = isExistMiningAccount;
    }

    public void setIsUserFundPassword(final Boolean isUserFundPassword) {
        this.isUserFundPassword = isUserFundPassword;
    }

    public void setIsSignedLVTRiskAgreement(final Boolean isSignedLVTRiskAgreement) {
        this.isSignedLVTRiskAgreement = isSignedLVTRiskAgreement;
    }

    public void setIsIsolatedMarginUser(final Boolean isIsolatedMarginUser) {
        this.isIsolatedMarginUser = isIsolatedMarginUser;
    }

    public void setIsExistIsolatedMarginAccount(final Boolean isExistIsolatedMarginAccount) {
        this.isExistIsolatedMarginAccount = isExistIsolatedMarginAccount;
    }

    public void setIsMobileUser(final Boolean isMobileUser) {
        this.isMobileUser = isMobileUser;
    }

    public void setIsUserNotBindEmail(final Boolean isUserNotBindEmail) {
        this.isUserNotBindEmail = isUserNotBindEmail;
    }
}