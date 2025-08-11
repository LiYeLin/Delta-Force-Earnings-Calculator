package com.sjz.lcsjz.common.dal.serializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 自定义BigDecimal序列化器，用于将其格式化为8位小数。
 */
public class BigDecimal8DigitSerializer extends JsonDeserializer<BigDecimal> {

    @Override
    public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        String value = p.getText();
        if (value != null && !value.isEmpty()) {
            // 精度8位，使用 HALF_UP 进行四舍五入
            return new BigDecimal(value).setScale(8, RoundingMode.HALF_UP);
        }
        return null;
    }
}