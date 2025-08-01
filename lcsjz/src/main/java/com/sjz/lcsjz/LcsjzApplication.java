package com.sjz.lcsjz;


import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.sjz.lcsjz.common.dal.mapper")
@EnableScheduling
@EnableRetry
public class LcsjzApplication {
    private static final Logger log = LoggerFactory.getLogger(LcsjzApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(LcsjzApplication.class, args);
		log.info("启动成功");
	}

}
