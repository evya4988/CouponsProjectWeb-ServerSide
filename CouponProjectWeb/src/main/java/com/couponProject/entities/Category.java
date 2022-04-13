package com.couponProject.entities;

public enum Category {

	FOOD(1, "Food"),
	ELECTRICITY(2, "Electricity"),
	RESTAURANT(3, "Restaurant"),
	VACATION(4, "Vacation");

	private int id;
	private String name;

	Category() {}
	
	Category(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
