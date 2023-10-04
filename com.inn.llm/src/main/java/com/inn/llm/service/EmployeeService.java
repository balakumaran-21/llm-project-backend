package com.inn.llm.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeService {

	ResponseEntity<String> signUp(Map<String, String> requestMap);
	
	ResponseEntity<String> login(String id, String password);
}
