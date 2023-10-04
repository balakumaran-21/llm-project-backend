package com.inn.llm.rest;


import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(path = "/employee")
@CrossOrigin(origins = "*")
public interface EmployeeRest {
	@PostMapping(path = "/signUp")
	public ResponseEntity<String> signUp(@RequestBody(required = true) Map<String, String> requestMap);
	
	@GetMapping(path = "/login/{id}/{password}")
	public ResponseEntity<String> login(@PathVariable int id, @PathVariable String password);
	
}
