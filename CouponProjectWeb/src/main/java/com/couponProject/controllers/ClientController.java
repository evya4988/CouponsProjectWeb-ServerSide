package com.couponProject.controllers;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.couponProject.main.TokenManager;

@Service
@Transactional
public abstract class ClientController {

	@Autowired
	public TokenManager tokenManager;

	public ClientController() {
	}

}
