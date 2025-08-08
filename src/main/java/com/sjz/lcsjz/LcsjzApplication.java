package com.sjz.lcsjz;


import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 嘎嘎板正/嘎嘎权威/牛而逼之的启动类
 *
 */
@SpringBootApplication
@MapperScan("com.sjz.lcsjz.common.dal.mapper")
@EnableScheduling
@EnableRetry
@Slf4j
public class LcsjzApplication {
	public static void main(String[] args) {
		SpringApplication.run(LcsjzApplication.class, args);
		log.info("启动成功");
	}

}
