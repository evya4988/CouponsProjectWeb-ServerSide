package com.couponProject.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.couponProject.entities.Administrator;
import com.couponProject.entities.Company;
import com.couponProject.entities.Coupon;
import com.couponProject.entities.Customer;
import com.couponProject.exceptions.CompanyAlreadyExistsException;
import com.couponProject.exceptions.CompanyNotFoundException;
import com.couponProject.exceptions.CustomerAlreadyExistsException;
import com.couponProject.exceptions.CustomerNotFoundException;

@Service
@Transactional
public class AdminService extends ClientService{

	@Autowired
	private Administrator admin;

	public AdminService() {
	}

	@Override
	public boolean login(String email, String password) {
		if (admin.getEmail().equals(email) && admin.getPassword().equals(password)) {
			return true;
		}
		return false;
	}

	public Company addCompany(Company company) throws CompanyAlreadyExistsException {
		List<Company> check = companyRepository.findByEmailOrName(company.getEmail(), company.getName());
		if (!check.isEmpty()) {
			throw new CompanyAlreadyExistsException("The company that you're trying to create already exists.");
		}
		return companyRepository.save(company);
	}

	public Company updateCompany(Company company) throws CompanyNotFoundException, CompanyAlreadyExistsException {
		List<Company> check = companyRepository.findByIdAndName(company.getId(), company.getName());
		if (check.isEmpty()) {
			throw new CompanyNotFoundException("The company that you're trying to update doesn't exist.");
		}
		List<Company> checkEmail = companyRepository.findByEmail(company.getEmail());
		if(!checkEmail.isEmpty()) {
			if(check.get(0).getEmail() != checkEmail.get(0).getEmail()) {
				throw new CompanyAlreadyExistsException("There is already a company with the same email!");
			}
		}
		return companyRepository.save(company);
	}

	public boolean deleteCompany(int companyID) throws CompanyNotFoundException {
		Company company = getOneCompany(companyID);
		if(company == null) {
			return false;
		}
		companyRepository.delete(company);
		return true;
	}

	public List<Company> getAllCompanies() throws CompanyNotFoundException {
		if(companyRepository.count() < 1) {
			throw new CompanyNotFoundException("There are no companies to show!");
		}
		return companyRepository.findAll();
	}

	public Company getOneCompany(int companyID) throws CompanyNotFoundException {
		List<Company> companies = companyRepository.findById(companyID);
		if (companies == null) {
			throw new CompanyNotFoundException("The company that you're trying to find doesn't exist.");
		}
		return companies.get(0);
	}

	public Customer addCustomer(Customer customer) throws CustomerAlreadyExistsException {
		List<Customer> check = customerRepository.findByEmail(customer.getEmail());
		if (!check.isEmpty()) {
			throw new CustomerAlreadyExistsException("The customer that you're trying to create already exists.");
		}
		return customerRepository.save(customer);
	}

	public Customer updateCustomer(Customer customer) throws CustomerNotFoundException, CustomerAlreadyExistsException {
		List<Customer> check = customerRepository.findById(customer.getId());
		if (check == null) {
			throw new CustomerNotFoundException("The customer that you're trying to update doesn't exist.");
		}
		List<Customer> checkEmail = customerRepository.findByEmail(customer.getEmail());
		if(!checkEmail.isEmpty()) {
			if(check.get(0).getEmail() != checkEmail.get(0).getEmail()) {
				throw new CustomerAlreadyExistsException("There is already a customer with the same email!");
			}
		}
		return customerRepository.save(customer);
	}

	public boolean deleteCustomer(int customerID) throws CustomerNotFoundException {
		Customer customer = getOneCustomer(customerID);
		if(customer == null) {
			return false;
		}
		for(Coupon coupon:customer.getCoupons()) {
			coupon.deleteCouponPurchase(customer);
		}
		customerRepository.delete(customer);
		return true;
	}

	public List<Customer> getAllCustomers() throws CustomerNotFoundException {
		if(customerRepository.count() < 1) {
			throw new CustomerNotFoundException("There are no customers to show!");
		}
		return customerRepository.findAll();
	}

	public Customer getOneCustomer(int customerID) throws CustomerNotFoundException {
		List<Customer> customers = customerRepository.findById(customerID);
		if (customers == null) {
			throw new CustomerNotFoundException("The customer that you're trying to find doesn't exist.");
		}
		return customers.get(0);
	}

}
