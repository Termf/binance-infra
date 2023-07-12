package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;

import javax.validation.constraints.NotEmpty;

/**
 * Keep Alive User Data Stream
 *
 * @author Li Fenggang
 * Date: 2020/8/9 2:16 下午
 */
public class PutUserDataStreamRequest extends ToString {

	private static final long serialVersionUID = 1L;

	@NotEmpty
	private String listenKey;
	private long accountId;

	public PutUserDataStreamRequest(@NotEmpty String listenKey, long accountId) {
		this.listenKey = listenKey;
		this.accountId = accountId;
	}

	public String getListenKey() {
		return listenKey;
	}

	public void setListenKey(String listenKey) {
		this.listenKey = listenKey;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}
}