package com.couponProject.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.couponProject.entities.Category;
import com.couponProject.entities.Coupon;
import com.couponProject.entities.Customer;
import com.couponProject.exceptions.CouponAlreadyExpiredException;
import com.couponProject.exceptions.CouponAlreadyPurchasedException;
import com.couponProject.exceptions.CouponNotFoundException;
import com.couponProject.exceptions.CouponNotInStockException;
import com.couponProject.services.CustomerService;

@RestController
@CrossOrigin("*")
@RequestMapping("/customer")
public class CustomerController extends ClientController {

	@Autowired
	private CustomerService customerService;

	public CustomerController() {
	}

	/*
	 * Checks if the coupon with the ID in the argument exists; If so, checks if the
	 * coupon is in stock, already purchades by the customer and is not expired; If
	 * all the conditions are false, the coupon will be purchased by the customer
	 * and adds a purchase history to the database.
	 */
	@PostMapping("/purchaseCoupon/{couponId}")
	public ResponseEntity<?> purchaseCoupon(@PathVariable int couponId, @RequestHeader("customerId") int customerId,
			@RequestHeader("token") String token) {
		try {
			System.out.println("Got a coupon to buy: #" + couponId + ", token=" + token);
			if (tokenManager.isTokenExist(token)) {
				Coupon purchasedCoupon = customerService.PurchaseCoupon(couponId, customerId);
				return new ResponseEntity<Integer>(purchasedCoupon.getId(), HttpStatus.OK);
			}
		} catch (CouponNotFoundException | CouponNotInStockException | CouponAlreadyPurchasedException
				| CouponAlreadyExpiredException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<String>("Something went wrong!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getCustomerCoupons")
	public ResponseEntity<?> getCustomerCoupons(@RequestHeader("customerId") int customerId,
			@RequestHeader("token") String token) {
		try {
			System.out.println("getting all the customer's coupons, token=" + token);
			if (tokenManager.isTokenExist(token)) {
				List<Coupon> customerCoupons = customerService.getCustomerCoupons(customerId);
				return new ResponseEntity<List<Coupon>>(customerCoupons, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>("Something went wrong!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/getPurchaseCoupons")
	public ResponseEntity<?> getPurchaseCoupons(@RequestHeader("customerId") int customerId,
			@RequestHeader("token") String token) {
		try {
			System.out.println("getting all the available coupons to purchase, token=" + token);
			if (tokenManager.isTokenExist(token)) {
				List<Coupon> purchaseCoupons = customerService.getPurchaseCoupons(customerId);
				return new ResponseEntity<List<Coupon>>(purchaseCoupons, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>("Something went wrong!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getCustomerCouponsByCategory/{category}")
	public ResponseEntity<?> getCustomerCoupons(@PathVariable int category, @RequestHeader("id") int customerId, @RequestHeader("token") String token) {
		try {
			Category convertedCategory = Category.values()[category];
			System.out.println("getting all the customer's coupons with the category: " + convertedCategory + ", token=" + token);
			if (tokenManager.isTokenExist(token)) {
				List<Coupon> customerCouponsByCategory = customerService.getCustomerCoupons(convertedCategory, customerId);
				return new ResponseEntity<List<Coupon>>(customerCouponsByCategory, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>("Something went wrong!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getCustomerCoupons/MaxPrice")
	public ResponseEntity<?> getCustomerCoupons(@RequestHeader("maxPrice") double maxPrice,
			@RequestHeader("id") int customerId, @RequestHeader("token") String token) {
		try {
			System.out
					.println("getting all the customer's coupons with the max price: " + maxPrice + ", token=" + token);
			if (tokenManager.isTokenExist(token)) {
				List<Coupon> customerCouponsByMaxPrice = customerService.getCustomerCoupons(maxPrice, customerId);
				return new ResponseEntity<List<Coupon>>(customerCouponsByMaxPrice, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>("Something went wrong!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getCustomerCoupons/MinPrice")
	public ResponseEntity<?> getCustomerCouponsByMinPrice(@RequestHeader("minPrice") double minPrice,
			@RequestHeader("id") int customerId, @RequestHeader("token") String token) {

		try {
			System.out
					.println("getting all the customer's coupons with the min price: " + minPrice + ", token=" + token);
			if (tokenManager.isTokenExist(token)) {
				List<Coupon> customerCouponsByMinPrice = customerService.getCustomerCouponsByMinPrice(minPrice,
						customerId);
				return new ResponseEntity<List<Coupon>>(customerCouponsByMinPrice, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>("Something went wrong!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getCustomerDetails")
	public ResponseEntity<?> getCustomerDetails(@RequestHeader("customerId") int customerId,
			@RequestHeader("token") String token) {

		try {
			System.out.println("getting the customer's details, token=" + token);
			if (tokenManager.isTokenExist(token)) {
				Customer customerCouponsByCategory = customerService.getCustomerDetails(customerId);
				return new ResponseEntity<Customer>(customerCouponsByCategory, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>("Something went wrong!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}
}