package com.sjz.lcsjz.common.integration.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class KkrbResp<T> {
    /**
     * 数据类型为T的属性data
     */
    private T data;
    /**
     * 状态码，表示操作结果的状态
     */
    private int code;
    /**
     * 状态信息，描述操作结果的详细信息
     */
    private String msg;
    /**
     * fixme 版本号 小时级别 需要更新到表里
     */
    @JsonFormat(pattern = "yyyyMMddHH")
    private LocalDateTime version;
}