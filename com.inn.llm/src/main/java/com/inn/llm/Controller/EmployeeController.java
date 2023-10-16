package com.inn.llm.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inn.llm.model.Employee;
import com.inn.llm.service.EmployeeService;

@RestController
@CrossOrigin(origins= "*")
@RequestMapping(path = "/employee")
public class EmployeeController{

	@Autowired
	EmployeeService service;
	
	@PostMapping(path = "/signUp")
	public ResponseEntity<String> signUp(@RequestBody Employee employee) {
		return service.signUp(employee);
	}
	
	@GetMapping(path = "login/{id}/{password}")
	public ResponseEntity<String> login(@PathVariable String id, @PathVariable String password) {
		return service.login(id, password);
	}
	
	@GetMapping(path = "/employeeByID/{id}")
	public ResponseEntity<Map<String,String>> getEmployeeByID(@PathVariable String id) {
		return service.getEmployeeByID(id);
	}
	
	@GetMapping(path = "/employeeByEmail/{email}")
	public ResponseEntity<Map<String,String>> getEmployeeByEmail(@PathVariable String email) {
		return service.getEmployeeByEmail(email);
	}
	
	@PutMapping(path = "/updateEmployee")
	public ResponseEntity<String> updateEmployee(@RequestBody Map<String,String> updateDetails){
		return service.updateEmployee(updateDetails);
	}
	
	@GetMapping(path = "/getAllEmployees")
	public ResponseEntity<List<Map<String,String>>> getAllEmployees(){
		return service.fetchAllEmployees();
	}
	
	@PutMapping(path = "/updateStatus")
	public ResponseEntity<String> updateStatus(@RequestBody Map<String,String> updateDetails){
		return service.updateStatus(updateDetails);
	}
	
	@DeleteMapping(path = "/deleteEmployee/{id}")
	public ResponseEntity<String> deleteEmployee(@PathVariable String id){
		return service.deleteEmployee(id);
	}
}
