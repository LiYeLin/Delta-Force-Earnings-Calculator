package com.sjz.lcsjz;


import org.apache.logging.slf4j.SLF4JLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LcsjzApplication {
    private static final Logger log = LoggerFactory.getLogger(LcsjzApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(LcsjzApplication.class, args);
		log.info("启动成功");
	}

}
