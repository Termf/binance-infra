package com.binance.platform.mbx.model.matchbox;

import com.binance.master.commons.ToString;
import com.binance.platform.mbx.util.FormatUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class PostBalanceBatchRequest extends ToString {

	private static final long serialVersionUID = 1L;

	@NotEmpty
	private List<Balance> balanceList;

	public List<Balance> getBalanceList() {
		return balanceList;
	}

	public void setBalanceList(List<Balance> balanceList) {
		this.balanceList = balanceList;
	}

	public static class Balance {
		private Long accountId;
		private String asset;
		private Long updateId;

		@JsonSerialize(using = BigDecimalToPlainStringSerializer.class)
		private BigDecimal balanceDelta;

		public Long getAccountId() {
			return accountId;
		}

		public void setAccountId(Long accountId) {
			this.accountId = accountId;
		}

		public String getAsset() {
			return asset;
		}

		public void setAsset(String asset) {
			this.asset = asset;
		}

		public Long getUpdateId() {
			return updateId;
		}

		public void setUpdateId(Long updateId) {
			this.updateId = updateId;
		}

		public BigDecimal getBalanceDelta() {
			return balanceDelta;
		}

		public void setBalanceDelta(BigDecimal balanceDelta) {
			this.balanceDelta = balanceDelta;
		}
	}

	public static class BigDecimalToPlainStringSerializer extends JsonSerializer<BigDecimal> {
		@Override
		public void serialize(BigDecimal value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
				JsonProcessingException {
			if (value != null) {
				jgen.writeString(FormatUtils.getAssetNumericFormatter().format(value));
			} else {
				jgen.writeNull();
			}
		}
	}

}