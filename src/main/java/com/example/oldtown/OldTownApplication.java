package com.example.oldtown;

import com.example.oldtown.util.ScheduledUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author dyp
 * @date 20201023
 */

@SpringBootApplication
@EnableScheduling
@MapperScan("com.example.oldtown.modules.*.mapper")
public class OldTownApplication {

	public static void main(String[] args) {
		SpringApplication.run(OldTownApplication.class, args);
	}

}
