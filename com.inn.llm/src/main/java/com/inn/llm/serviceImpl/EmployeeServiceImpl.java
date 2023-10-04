package com.inn.llm.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Objects;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.common.base.Optional;
import com.inn.llm.constants.LLMConstants;
import com.inn.llm.dao.EmployeeDAO;
import com.inn.llm.model.Employee;
import com.inn.llm.service.*;
import com.inn.llm.utils.LLMUtils;

@Service
public class EmployeeServiceImpl implements EmployeeService{
	
	@Autowired
	EmployeeDAO employeeDAO;
	//New User sign up
	@Override
	public ResponseEntity<String> signUp(Map<String, String> requestMap) {
		
		if(validateSignUp(requestMap)) {
			Employee employee = employeeDAO.findByEmailId(requestMap.get("email"));
			if(Objects.isNull(employee)) {
				employeeDAO.save(getEmployeeFromMap(requestMap));
				return LLMUtils.getResponseEntity(requestMap.get("name")+" Registration sucessfull !!", HttpStatus.OK);
			}else {
				return LLMUtils.getResponseEntity("Email ID already exists !!", HttpStatus.OK);
			}
		}
		else {
			return LLMUtils.getResponseEntity("Something went wrong unable to register", HttpStatus.OK);
		}
//		return LLMUtils.getResponseEntity(LLMConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private boolean validateSignUp(Map<String, String>requestMap) {
		if(requestMap.containsKey("name") && requestMap.containsKey("email") && 
				requestMap.containsKey("role") && requestMap.containsKey("password")) {
			return true;
		}
		return false;
	}
	
	
	private Employee getEmployeeFromMap(Map<String,String>requestMap) {
		Employee employee = new Employee();
		Random random = new Random();
		employee.setEmployee_id(random.nextInt(10000,99999));
		employee.setName(requestMap.get("name"));
		employee.setEmail(requestMap.get("email"));
		employee.setRole(requestMap.get("role"));
		employee.setPassword(requestMap.get("password"));
		employee.setStatus(
				employee.getRole().equals("Admin")?"true":"false"
				);
		return employee;
	}

	@Override
	public ResponseEntity<String> login(String id, String password) {
		
//		Optional<Employee> current = employeeDAO.findByEmailId(employee.getEmail());

//		Optional<Employee> current1 = employeeDAO.findStudentByPhone(user.getEmail());
		return null;
	}

}
