package com.xe.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xe.demo.common.datasource.DynamicDataSourceRegister;

/**
 * springboot启动器
 */
@Controller
@EnableCaching// 开启缓存
@MapperScan(basePackages = "com.xe.*.mapper")//扫描映射接口
@Import(DynamicDataSourceRegister.class)//多数据源设置
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@RequestMapping("/")
	String home() {
		return "login";
	}

	@RequestMapping("/404")
	String notFound() {
		return "common/404";
	}

	@RequestMapping("/500")
	String error() {
		return "common/500";
	}
}
