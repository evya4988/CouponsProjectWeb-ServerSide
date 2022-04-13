package com.couponProject.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.couponProject.entities.Category;
import com.couponProject.entities.Company;
import com.couponProject.entities.Coupon;
import com.couponProject.entities.CouponFiller;
import com.couponProject.exceptions.CouponAlreadyExistsException;
import com.couponProject.exceptions.CouponNotFoundException;
import com.couponProject.exceptions.DataNotValidException;
import com.couponProject.services.CompanyService;

@RestController
@CrossOrigin("*")
@RequestMapping("/company")
public class CompanyController extends ClientController {

	@Autowired
	private CompanyService companyService;

	public CompanyController() {
	}

	@PostMapping("/addCoupon")
	public ResponseEntity<?> addCoupon(@RequestBody CouponFiller filler, @RequestHeader("token") String token) {
		try {
			Coupon coupon = filler.convert();
			coupon.setCompany(filler.getCompanyId());
			System.out.println("Got a new coupon: " + coupon + ", token=" + token);
			if (tokenManager.isTokenExist(token)) {
				Coupon savedCoupon = companyService.addCoupon(coupon);
				return new ResponseEntity<Coupon>(savedCoupon, HttpStatus.OK);
			}
		} catch (CouponAlreadyExistsException | CouponNotFoundException | DataNotValidException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<String>("Something went wrong!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/updateCoupon")
	public ResponseEntity<?> updateCoupon(@RequestBody CouponFiller filler, @RequestHeader("token") String token) {
		try {
			Coupon coupon = filler.convertWithID();
			coupon.setCompany(filler.getCompanyId());
			System.out.println("Got a coupon to update: " + coupon + ", token=" + token);
			if (tokenManager.isTokenExist(token)) {
				companyService.updateCoupon(coupon);
				String categoryName = coupon.getCategory().name();
				return new ResponseEntity<String>(categoryName, HttpStatus.OK);
			}
		} catch (CouponAlreadyExistsException | CouponNotFoundException | DataNotValidException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<String>("Something went wrong!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("/deleteCoupon/{couponID}")
	public ResponseEntity<?> deleteCoupon(@PathVariable int couponID, @RequestHeader("companyId") int companyId,
			@RequestHeader("token") String token) {
		try {
			System.out.println("Got a coupon's ID to delete: #" + couponID + ", token=" + token);
			if (tokenManager.isTokenExist(token)) {
				boolean status = companyService.deleteCoupon(couponID, companyId);
				return new ResponseEntity<Boolean>(status, HttpStatus.OK);
			}
		} catch (CouponNotFoundException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<String>("Something went wrong!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getOneCoupon/{couponID}")
	public ResponseEntity<?> getOneCoupon(@PathVariable int couponID, @RequestHeader("companyId") int companyId,
			@RequestHeader("token") String token) {
		try {
			System.out.println("getting a coupon by ID: #" + couponID + ", token=" + token);
			if (tokenManager.isTokenExist(token)) {
				Coupon requestedCoupon = companyService.getOneCoupon(couponID, companyId);
				return new ResponseEntity<Coupon>(requestedCoupon, HttpStatus.OK);
			}
		} catch (CouponNotFoundException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<String>("Something went wrong!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getCompanyCoupons/{companyId}")
	public ResponseEntity<?> getCompanyCoupons(@PathVariable int companyId, @RequestHeader("token") String token) {
		try {
			System.out.println("getting all the company's coupons, token=" + token);
			if (tokenManager.isTokenExist(token)) {
				List<Coupon> companyCoupons = companyService.getCompanyCoupons(companyId);
				return new ResponseEntity<List<Coupon>>(companyCoupons, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>("Something went wrong!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getCompanyCouponsByCategory/{category}")
	public ResponseEntity<?> getCompanyCouponsByCategory(@PathVariable int category, @RequestHeader("id") int companyId,
			@RequestHeader("token") String token) {
		try {
			Category convertedCategory = Category.values()[category];
			System.out.println("getting all the company's coupons with the category: " + convertedCategory + ", token=" + token);
			if (tokenManager.isTokenExist(token)) {
				List<Coupon> companyCouponsByCategory = companyService.getCompanyCoupons(convertedCategory, companyId);
				return new ResponseEntity<List<Coupon>>(companyCouponsByCategory, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>("Something went wrong!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getCompanyCoupons/MaxPrice")
	public ResponseEntity<?> getCompanyCoupons(@RequestHeader("maxPrice") Double maxPrice,
			@RequestHeader("companyId") int companyId, @RequestHeader("token") String token) {
		try {
			System.out.println(companyId);
			System.out.println(maxPrice);
			System.out.println("getting all the company's coupons with the max price: " + maxPrice + ", token=" + token);
			if (tokenManager.isTokenExist(token)) {
				List<Coupon> companyCouponsByMaxPrice = companyService.getCompanyCoupons(maxPrice, companyId);
				return new ResponseEntity<List<Coupon>>(companyCouponsByMaxPrice, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>("Something went wrong!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getCompanyCoupons/MinPrice")
	public ResponseEntity<?> getCompanyCouponsByMinPrice(@RequestHeader("minPrice") double minPrice,
			@RequestHeader("companyId") int companyId, @RequestHeader("token") String token) {
		try {
			System.out
					.println("getting all the company's coupons with the min price: " + minPrice + ", token=" + token);
			if (tokenManager.isTokenExist(token)) {
				List<Coupon> companyCouponsByMinPrice = companyService.getCompanyCouponsByMinPrice(minPrice, companyId);
				return new ResponseEntity<List<Coupon>>(companyCouponsByMinPrice, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>("Something went wrong!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getCompanyDetails")
	public ResponseEntity<?> getCompanyDetails(@RequestHeader("token") String token) {
		try {
			System.out.println("getting the company's details, token=" + token);
			if (tokenManager.isTokenExist(token)) {
				Company compmanyDetails = companyService.getCompanyDetails();
				return new ResponseEntity<Company>(compmanyDetails, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>("Something went wrong!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}
}