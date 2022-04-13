package com.couponProject.services;

import java.util.Calendar;
import java.util.Date;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.couponProject.repositories.CompanyRepository;
import com.couponProject.repositories.CouponRepository;
import com.couponProject.repositories.CustomerRepository;

@Service
@Transactional
public abstract class ClientService {
	
	@Autowired
	protected CouponRepository couponRepository;
	@Autowired
	protected CompanyRepository companyRepository;
	@Autowired
	protected CustomerRepository customerRepository;
	
	public ClientService() {}
	
	public abstract boolean login(String email, String password);
	
	public boolean isCouponValid(Date start, Date end, double price, int amount) {
		if(!(isValidDate(start) && isEndDateValid(start, end) && isAmountValid(amount) && isPriceValid(price))) {
			return false;
		}
		return true;
	}
	
	public boolean isValidDate(Date date) {
		if(date.before(Calendar.getInstance().getTime())) {
			return false;
		}
		return true;
	}
	
	public boolean isEndDateValid(Date start, Date end) {
		if(end.before(start)) {
			return false;
		}
		return true;
	}
	
	public boolean isAmountValid(int amount) {
		if(amount <= 0) {
			return false;
		}
		return true;
	}
	
	public boolean isPriceValid(double price) {
		if(price <= 0) {
			return false;
		}
		return true;
	}
}
