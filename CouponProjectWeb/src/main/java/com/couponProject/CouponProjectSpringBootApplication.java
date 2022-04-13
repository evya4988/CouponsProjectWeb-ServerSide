package com.couponProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import com.couponProject.main.StartJob;

@SpringBootApplication
public class CouponProjectSpringBootApplication {
	
	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(CouponProjectSpringBootApplication.class, args);
		StartJob startJob = ctx.getBean(StartJob.class);
		startJob.activate();
	}
}