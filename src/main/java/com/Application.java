
package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.redis.JedisUtil;

// 使用Spring Boot框架的Java應用程式的主要入口點
// @SpringBootApplication：這是一個合併註解，它包含多個其他註解\
// 包括 @Configuration、@EnableAutoConfiguration 和 @ComponentScan。
// 這裡的@SpringBootApplication 表明這是一個Spring Boot應用程式的主類別
// 同時它啟用了自動配置和組件掃描。
@SpringBootApplication
	public class Application {

	public static void main(String[] args) {
		// 啟動Spring Application
		SpringApplication.run(Application.class, args);
	}
	

}


