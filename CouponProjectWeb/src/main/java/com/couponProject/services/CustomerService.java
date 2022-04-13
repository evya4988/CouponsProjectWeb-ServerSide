package com.couponProject.services;

import java.util.Calendar;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.couponProject.entities.Category;
import com.couponProject.entities.Coupon;
import com.couponProject.entities.Customer;
import com.couponProject.exceptions.CouponAlreadyExpiredException;
import com.couponProject.exceptions.CouponAlreadyPurchasedException;
import com.couponProject.exceptions.CouponNotFoundException;
import com.couponProject.exceptions.CouponNotInStockException;

@Scope("prototype")
@Service
@Transactional
public class CustomerService extends ClientService {

	private int customerID;

	public CustomerService(int customerID) {
		this.customerID = customerID;
	}

	public CustomerService() {
	}

	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	@Override
	public boolean login(String email, String password) {
		Customer customer = customerRepository.findByEmailAndPassword(email, password);
		if (customer == null) {
			return false;
		}
		setCustomerID(customer.getId());
		return true;
	}

	/*
	 * Checks if the coupon with the ID in the argument exists;
	 * If so, checks if the coupon is in stock, already purchades by the customer and is not expired;
	 * If all the conditions are false, the coupon will be purchased by
	 * the customer and adds a purchase history to the database.
	 */
	public Coupon PurchaseCoupon(int couponID, int customerID) throws CouponNotFoundException, CouponNotInStockException, CouponAlreadyPurchasedException, CouponAlreadyExpiredException {
		List<Coupon> check = couponRepository.findById(couponID);
		if (check == null) {
			throw new CouponNotFoundException("The coupon that you're trying to purchase doesn't exist.");
		}
		Coupon coupon = check.get(0);
		if (coupon.getAmount() <= 0) {
			throw new CouponNotInStockException("the coupon with ID " + coupon.getId() + " is not in stock!");
		}
		
		Customer customer = getCustomerDetails(customerID);
		if (getCustomerCoupons(customerID).contains(coupon)) {
			throw new CouponAlreadyPurchasedException(
					"the coupon with ID " + coupon.getId() + " has already " + "been purchased by the customer!");
		}

		if (coupon.getEndDate().before(Calendar.getInstance().getTime())) {
			throw new CouponAlreadyExpiredException("the coupon with ID " + coupon.getId() + " has been expired!");
		}
		coupon.addCouponPurchase(customer);
		customer.purchaseCoupon(coupon);
		coupon.setAmount(coupon.getAmount()-1);
		return couponRepository.save(coupon);
	}

	public List<Coupon> getCustomerCoupons(int customerID) {
		return getCustomerDetails(customerID).getCoupons();
	}
	
	public List<Coupon> getPurchaseCoupons(int customerID) {
		List<Coupon> customerCoupons = getCustomerCoupons(customerID);
		List<Coupon> allCoupons = couponRepository.findAll();
		allCoupons.removeAll(customerCoupons);
		System.out.println(allCoupons);
		return allCoupons; 
	}

	public List<Coupon> getCustomerCoupons(Category category, int customerID) {
		List<Coupon> customerCoupons = getCustomerCoupons(customerID);
		customerCoupons.retainAll(couponRepository.findByCategory(category));
		return customerCoupons;
	}

	public List<Coupon> getCustomerCoupons(double maxPrice, int customerID) {
		List<Coupon> customerCoupons = getCustomerCoupons(customerID);
		customerCoupons.retainAll(couponRepository.findByPriceLessThanEqual(maxPrice));
		return customerCoupons;
	}

	public List<Coupon> getCustomerCouponsByMinPrice(double minPrice, int customerID) {
		List<Coupon> customerCoupons = getCustomerCoupons(customerID);
		customerCoupons.retainAll(couponRepository.findByPriceGreaterThanEqual(minPrice));
		return customerCoupons;
	}

	public Customer getCustomerDetails(int customerID) {
		return customerRepository.getById(customerID);
	}
}