package com.couponProject.main;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.couponProject.services.AdminService;
import com.couponProject.services.CompanyService;
import com.couponProject.services.CustomerService;

@RestController
@CrossOrigin("*")
@RequestMapping("login")
public class LoginManager {

	@Autowired
	private ApplicationContext ctx;
	@Autowired
	private TokenManager tokenManager;
	@Autowired
	private AdminService adminService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Credentials cred) {
		System.out.println(new Date()+": Got a new login: "+cred);
		switch (cred.getRole()) {
		case "admin":
			if(adminService.login(cred.getEmail(), cred.getPassword())) {
				String token = tokenManager.getNewToken();
				Map<String, String> returnValue = new HashMap<>();
				returnValue.put("token", token);
				returnValue.put("name", "Admin");
				returnValue.put("id", "0");
				return new ResponseEntity<Map<String, String>>(returnValue, HttpStatus.OK);
			}
			break;
		case "company":
			CompanyService compService = ctx.getBean(CompanyService.class);
			if(compService.login(cred.getEmail(), cred.getPassword())) {
				int companyID = compService.getCompanyID();
				String token = tokenManager.getNewToken();
				Map<String, String> returnValue = new HashMap<>();
				returnValue.put("token", token);
				returnValue.put("name", compService.getCompanyDetails().getName());
				returnValue.put("id", companyID+"");
				return new ResponseEntity<Map<String, String>>(returnValue, HttpStatus.OK);
			}
			break;
		case "customer":
			CustomerService custService = ctx.getBean(CustomerService.class);
			if(custService.login(cred.getEmail(), cred.getPassword())) {
				int customerID = custService.getCustomerID();
				String token = tokenManager.getNewToken();
				Map<String, String> returnValue = new HashMap<>();
				returnValue.put("token", token);
				returnValue.put("name", custService.getCustomerDetails(customerID).getFirstName() + " " + 
						custService.getCustomerDetails(customerID).getLastName());
				returnValue.put("id", customerID+"");
				return new ResponseEntity<Map<String, String>>(returnValue, HttpStatus.OK);
			}
			break;
		}
		return new ResponseEntity<String>("Unsuccessful Login!", HttpStatus.BAD_REQUEST);
	}
}