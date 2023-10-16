package com.inn.llm.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.inn.llm.model.Employee;

public class LLMUtils {
	
	public static ResponseEntity<String> getResponseEntity(String responseMessage,HttpStatus httpStatus){
		return new ResponseEntity<String>("{\"message\":\"" + ""+responseMessage+"" + "\"}" , httpStatus);		
	}
	
	public static ResponseEntity<Employee> getResponseEntity(Employee employee, HttpStatus httpStatus){
		return new ResponseEntity<Employee>(employee , httpStatus);
	}
	
}
