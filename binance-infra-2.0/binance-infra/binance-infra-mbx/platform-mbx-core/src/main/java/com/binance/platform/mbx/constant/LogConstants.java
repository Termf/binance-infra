package com.binance.platform.mbx.constant;

/**
 * Constants for log.
 */
public interface LogConstants {
    /** the delimiter between log marks */
    String LOG_MARK_DELIMITER = "-";

    /** Root prefix of the SDK */
    String PREFIX_ROOT = "MBX_SDK";
    /** Input or output params */
    String PREFIX_IO = "IO";
    /** RPC */
    String PREFIX_RPC = "RPC";
    /** Mbx api sender */
    String PREFIX_MBX = PREFIX_IO + LOG_MARK_DELIMITER + "MBX";
    /** mbx api rpc */
    String PREFIX_MBX_RPC = PREFIX_MBX + LOG_MARK_DELIMITER + PREFIX_RPC;
    /** Service api in the cloud */
    String PREFIX_SERVICE = PREFIX_IO + LOG_MARK_DELIMITER + "SERVICE";
    /** Service api rpc */
    String PREFIX_SERVICE_RPC = PREFIX_SERVICE + LOG_MARK_DELIMITER + PREFIX_RPC;
}
