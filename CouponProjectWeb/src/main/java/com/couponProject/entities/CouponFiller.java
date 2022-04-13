package com.couponProject.entities;

import java.sql.Date;
import org.springframework.stereotype.Component;

@Component
public class CouponFiller {
	
	private int id;
	private int companyId;
	private int category;
	private String title;
	private String description;
	private String startDate;
	private String endDate;
	private int amount;
	private double price;
	private String image;
	
	public CouponFiller() {}
	
	public CouponFiller(int id, int companyId, int category, String title, String description, String startDate,
			String endDate, int amount, double price, String image) {
		this.id = id;
		this.companyId = companyId;
		this.category = category;
		this.title = title;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
		this.price = price;
		this.image = image;
	}

	public CouponFiller(int companyId, int category, String title, String description, String startDate, String endDate,
			int amount, double price, String image) {
		this.companyId = companyId;
		this.category = category;
		this.title = title;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
		this.price = price;
		this.image = image;
	}
	
	
	public int getId() {
		return id;
	}

	public int getCategory() {
		return category;
	}


	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}


	public int getAmount() {
		return amount;
	}


	public double getPrice() {
		return price;
	}


	public String getImage() {
		return image;
	}

	public int getCompanyId() {
		return companyId;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public Coupon convert() {
		Date startDate = Date.valueOf(this.startDate);
		Date endDate = Date.valueOf(this.endDate);
		Category category = Category.values()[this.category];
		return new Coupon(new Company(), category, this.title, this.description,
				startDate, endDate, this.amount, this.price, this.image);
	}
	
	public Coupon convertWithID() {
		Date startDate = Date.valueOf(this.startDate);
		Date endDate = Date.valueOf(this.endDate);
		Category category = Category.values()[this.category];
		return new Coupon(this.id, new Company(), category, this.title, this.description,
				startDate, endDate, this.amount, this.price, this.image);
	}
}