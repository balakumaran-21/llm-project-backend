package com.inn.llm.restImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.inn.llm.constants.LLMConstants;
import com.inn.llm.rest.EmployeeRest;
import com.inn.llm.service.EmployeeService;
import com.inn.llm.utils.LLMUtils;

@RestController
public class EmployeeRestImpl implements EmployeeRest{

	@Autowired
	EmployeeService employeeService;
	
	@Override
	public ResponseEntity<String> signUp(Map<String, String> requestMap) {
		try {
			return employeeService.signUp(requestMap);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return LLMUtils.getResponseEntity(LLMConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> login(int id, String password) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
