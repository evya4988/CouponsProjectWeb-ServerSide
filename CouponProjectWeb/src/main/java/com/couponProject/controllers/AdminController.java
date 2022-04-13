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

import com.couponProject.entities.Company;
import com.couponProject.entities.Customer;
import com.couponProject.exceptions.CompanyAlreadyExistsException;
import com.couponProject.exceptions.CompanyNotFoundException;
import com.couponProject.exceptions.CustomerAlreadyExistsException;
import com.couponProject.exceptions.CustomerNotFoundException;
import com.couponProject.services.AdminService;

@RestController
@CrossOrigin("*")
@RequestMapping("admin")
public class AdminController extends ClientController {

	@Autowired
	private AdminService adminService;

	public AdminController() {
	}

	@PostMapping("/addCompany")
	public ResponseEntity<?> addCompany(@RequestBody Company company, @RequestHeader("token") String token) {
		try {
			System.out.println("Got a new company: " + company + ", token=" + token);
			if (tokenManager.isTokenExist(token)) {
				Company savedCompany = adminService.addCompany(company);
				return new ResponseEntity<Company>(savedCompany, HttpStatus.OK);
			}
		} catch (CompanyAlreadyExistsException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<String>("Something went wrong!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/updateCompany")
	public ResponseEntity<?> updateCompany(@RequestBody Company company, @RequestHeader("token") String token) {
		try {
			System.out.println("Got a company to update: " + company + ", token=" + token);
			if (tokenManager.isTokenExist(token)) {
				Company updatedCompany = adminService.updateCompany(company);
				return new ResponseEntity<Integer>(updatedCompany.getId(), HttpStatus.OK);
			}
		} catch (CompanyNotFoundException | CompanyAlreadyExistsException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<String>("Something went wrong!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("/deleteCompany/{companyID}")
	public ResponseEntity<?> deleteCompany(@PathVariable int companyID, @RequestHeader("token") String token) {
		try {
			System.out.println("Got a company's ID to delete: #" + companyID + ", token=" + token);
			if (tokenManager.isTokenExist(token)) {
				boolean status = adminService.deleteCompany(companyID);
				return new ResponseEntity<Boolean>(status, HttpStatus.OK);
			}
		} catch (CompanyNotFoundException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<String>("Something went wrong!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getAllCompanies")
	public ResponseEntity<?> getAllCompanies(@RequestHeader("token") String token) {
		try {
			System.out.println("getting all the companies, token=" + token);
			if (tokenManager.isTokenExist(token)) {
				List<Company> allCompanies = adminService.getAllCompanies();
				return new ResponseEntity<List<Company>>(allCompanies, HttpStatus.OK);
			}
		} catch (CompanyNotFoundException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<String>("Something went wrong!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getOneCompany/{companyID}")
	public ResponseEntity<?> getOneCompany(@PathVariable int companyID, @RequestHeader("token") String token) {
		try {
			System.out.println("getting a company by ID: #" + companyID + ", token=" + token);
			if (tokenManager.isTokenExist(token)) {
				Company requestedCompany = adminService.getOneCompany(companyID);
				return new ResponseEntity<Company>(requestedCompany, HttpStatus.OK);
			}
		} catch (CompanyNotFoundException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<String>("Something went wrong!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/addCustomer")
	public ResponseEntity<?> addCustomer(@RequestBody Customer customer, @RequestHeader("token") String token) {
		try {
			System.out.println("Got a new customer: " + customer + ", token=" + token);
			if (tokenManager.isTokenExist(token)) {
				Customer savedCustomer = adminService.addCustomer(customer);
				return new ResponseEntity<Integer>(savedCustomer.getId(), HttpStatus.OK);
			}
		} catch (CustomerAlreadyExistsException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<String>("Something went wrong!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/updateCustomer")
	public ResponseEntity<?> updateCustomer(@RequestBody Customer customer, @RequestHeader("token") String token) {
		try {
			System.out.println("Got a customer to update: " + customer + ", token=" + token);
			if (tokenManager.isTokenExist(token)) {
				Customer updatedCustomer = adminService.updateCustomer(customer);
				return new ResponseEntity<Integer>(updatedCustomer.getId(), HttpStatus.OK);
			}
		} catch (CustomerNotFoundException | CustomerAlreadyExistsException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<String>("Something went wrong!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("/deleteCustomer/{customerID}")
	public ResponseEntity<?> deleteCustomer(@PathVariable int customerID, @RequestHeader("token") String token) {
		try {
			System.out.println("Got a customer's ID to delete: #" + customerID + ", token=" + token);
			if (tokenManager.isTokenExist(token)) {
				boolean status = adminService.deleteCustomer(customerID);
				return new ResponseEntity<Boolean>(status, HttpStatus.OK);
			}
		} catch (CustomerNotFoundException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<String>("Something went wrong!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getAllCustomers")
	public ResponseEntity<?> getAllCustomers(@RequestHeader("token") String token) {
		try {
			System.out.println("getting all the customers, token=" + token);
			if (tokenManager.isTokenExist(token)) {
				List<Customer> allCustomers = adminService.getAllCustomers();
				return new ResponseEntity<List<Customer>>(allCustomers, HttpStatus.OK);
			}
		} catch (CustomerNotFoundException e) {
				return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<String>("Something went wrong!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getOneCustomer/{customerID}")
	public ResponseEntity<?> getOneCustomer(@PathVariable int customerID, @RequestHeader("token") String token) {
		try {
			System.out.println("getting a customer by ID: #" + customerID + ", token=" + token);
			if (tokenManager.isTokenExist(token)) {
				Customer requestedCustomer = adminService.getOneCustomer(customerID);
				return new ResponseEntity<Customer>(requestedCustomer, HttpStatus.OK);
			}
		} catch (CustomerNotFoundException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<String>("Something went wrong!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("No Session!", HttpStatus.BAD_REQUEST);
	}

}