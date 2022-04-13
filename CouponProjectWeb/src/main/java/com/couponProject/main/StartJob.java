package com.couponProject.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartJob {
	
	@Autowired
	private CouponExpirationDailyJob job;
	
	public void activate() {
		Thread t = new Thread(job);
		t.setDaemon(true);
		t.start();
	}
}
