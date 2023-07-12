package com.binance.platform.openfeign.jackson;

import java.io.IOException;
import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;

public class MyBigDecimalDeserializer extends StdScalarDeserializer<BigDecimal> {
	public final static MyBigDecimalDeserializer instance = new MyBigDecimalDeserializer();

	public MyBigDecimalDeserializer() {
		super(BigDecimal.class);
	}

	@Override
	public Object getEmptyValue(DeserializationContext ctxt) {
		return BigDecimal.ZERO;
	}

	@Override
	public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		switch (p.getCurrentTokenId()) {
		case JsonTokenId.ID_NUMBER_INT:
		case JsonTokenId.ID_NUMBER_FLOAT:
			return p.getDecimalValue();
		case JsonTokenId.ID_STRING:
			String text = p.getText().trim();
			if (_isEmptyOrTextualNull(text)) {
				_verifyNullForScalarCoercion(ctxt, text);
				return getNullValue(ctxt);
			}
			_verifyStringForScalarCoercion(ctxt, text);
			try {
				if (text != null) {
					if (text.length() > 100) {
						throw new RuntimeException("Invalid numeric value " + text);
					}
					int indexOfE = validateAndGetIndexOfE(text.toLowerCase());
					if (indexOfE > 0) {
						String[] textParts = StringUtils.split(text.toLowerCase(), "e");
						int firstNonZeroPosition = getFirstNonZeroPosition(textParts[0]);
						long exponent = Long.valueOf(textParts[1]);
						if (firstNonZeroPosition + exponent > 32 || firstNonZeroPosition + exponent < -8) {
							throw new RuntimeException("Invalid numeric value " + text);
						}
					}
				}
				return new BigDecimal(text);
			} catch (IllegalArgumentException iae) {
			}
			return (BigDecimal) ctxt.handleWeirdStringValue(_valueClass, text, "not a valid representation");
		case JsonTokenId.ID_START_ARRAY:
			return _deserializeFromArray(p, ctxt);
		}
		return (BigDecimal) ctxt.handleUnexpectedToken(_valueClass, p);
	}

	private static int getFirstNonZeroPosition(String decimalStr) {
		int dotIndex = decimalStr.indexOf('.');
		int nonZeroIndex = -1;
		char[] chars = decimalStr.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] > '0' && chars[i] != '-' && chars[i] != '.' && chars[i] != '+') {
				nonZeroIndex = i;
				break;
			}
		}
		return dotIndex > 0 ? dotIndex - nonZeroIndex : chars.length - nonZeroIndex;
	}

	private static int validateAndGetIndexOfE(String text) {
		char[] chars = text.toCharArray();
		int index = -1;
		for (int i = 0; i < text.length(); i++) {
			if (chars[i] == 'e') {
				// 包含多个e， 或者e在第一位或者最后一位
				if (index > 0 || i == 0 || i == text.length() - 1) {
					throw new RuntimeException("Invalid numeric value " + text);
				}
				index = i;
				continue;
			}
			// 是否是 纯数字 或者 负数符号 或者 小数点
			if ((chars[i] < '0' || chars[i] > '9') && chars[i] != '-' && chars[i] != '.' && chars[i] != '+') {
				throw new RuntimeException("Invalid numeric value " + text);
			}
		}
		return index;
	}

}
