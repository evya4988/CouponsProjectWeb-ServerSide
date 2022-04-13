package com.couponProject.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.couponProject.entities.Category;
import com.couponProject.entities.Company;
import com.couponProject.entities.Coupon;
import com.couponProject.exceptions.CouponAlreadyExistsException;
import com.couponProject.exceptions.CouponNotFoundException;
import com.couponProject.exceptions.DataNotValidException;

@Scope("prototype")
@Service
@Transactional
public class CompanyService extends ClientService {
	
	private int companyID;
	
	public CompanyService(int companyID) {
		this.companyID = companyID;
	}
	
	public CompanyService() {}
	
	public int getCompanyID() {
		return companyID;
	}

	public void setCompanyID(int companyID) {
		this.companyID = companyID;
	}
	
	@Override
	public boolean login(String email, String password) {
		Company company = companyRepository.findByEmailAndPassword(email, password);
		if(company == null) {
			return false;
		}
		setCompanyID(company.getId());
		return true;
	}
	
	
	public Coupon addCoupon(Coupon coupon) throws CouponAlreadyExistsException, CouponNotFoundException, DataNotValidException {
		List<Coupon> check = couponRepository.findByTitle(coupon.getTitle());
		if(!check.isEmpty()) {
			throw new CouponAlreadyExistsException("There is already a coupon with the same title!");
		}
		if(!isCouponValid(coupon.getStartDate(), coupon.getEndDate(), coupon.getPrice(), coupon.getAmount())) {
			throw new DataNotValidException("One of the parameters isn't valid, Try again!");
		}
		return couponRepository.save(coupon);
	}
	
	public Coupon updateCoupon(Coupon coupon) throws CouponNotFoundException, CouponAlreadyExistsException, DataNotValidException {
		List<Coupon> check = couponRepository.findByIdAndCompanyId(coupon.getId(), coupon.getCompany());
		if(check.isEmpty()) {
			throw new CouponNotFoundException("The coupon that you tried to update doesn't exist.");
		}
		if(!isCouponValid(coupon.getStartDate(), coupon.getEndDate(), coupon.getPrice(), coupon.getAmount())) {
			throw new DataNotValidException("One of the parameters isn't valid, Try again!");
		}
		List<Coupon> checkTitle = couponRepository.findByTitle(coupon.getTitle());
		if(!checkTitle.isEmpty()) {
			if(check.get(0).getTitle() != checkTitle.get(0).getTitle()) {
				throw new CouponAlreadyExistsException("There is already a coupon with the same title!");
			}
		}
		return couponRepository.save(coupon);
	}
	
	public boolean deleteCoupon(int couponID, int companyID) throws CouponNotFoundException {
		Coupon coupon = getOneCoupon(couponID, companyID);
		if(coupon == null) {
			return false;
		}
		couponRepository.delete(coupon);
		return true;
	}
	
	public Coupon getOneCoupon(int couponID, int companyID) throws CouponNotFoundException {
		List<Coupon> check = couponRepository.findByIdAndCompanyId(couponID, companyID);
		if (check.isEmpty()) {
			throw new CouponNotFoundException("The coupon that you tried to get doesn't exist in this company.");
		}
		return couponRepository.getById(couponID);
	}
	
	public List<Coupon> getCompanyCoupons(int companyId) {
		return couponRepository.findByCompanyId(companyId);
	}

	public List<Coupon> getCompanyCoupons(Category category, int companyId) {
		return couponRepository.findByCompanyIdAndCategory(companyId, category);
	}

	public List<Coupon> getCompanyCoupons(double maxPrice, int companyId) {
		return couponRepository.findByCompanyIdAndPriceLessThanEqual(companyId, maxPrice);
	}

	public List<Coupon> getCompanyCouponsByMinPrice(double minPrice, int companyId) {
		return couponRepository.findByCompanyIdAndPriceGreaterThanEqual(companyId, minPrice);
	}
	
	public Company getCompanyDetails() {
		return companyRepository.getById(companyID);
	}
}