package com.inn.llm.service;

import java.util.Optional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.inn.llm.dao.EmployeeDAO;
import com.inn.llm.model.Employee;
import com.inn.llm.utils.LLMUtils;

@Service
public class EmployeeService{
	
	@Autowired
	EmployeeDAO employeeDAO;
	
	
	public Employee addEmployee(Employee employee) {
		Random random = new Random();
		Integer id = random.nextInt(10000,99999);
		employee.setEmployee_id(id.toString());
		String status = (employee.getRole().equals("Admin"))?"true":"false";
		employee.setStatus(status);
		return employeeDAO.save(employee);
	}
	
	public boolean checkEmployeeEmail(String email) {
		Employee employee = employeeDAO.findByEmailId(email);
		if(Objects.isNull(employee)) {
			return false;
		}
		return true;
	}
	
	
	/********************************* Rest API functionalities ***********************************/
	
	public ResponseEntity<String> signUp(Employee employee) {
		if(!checkEmployeeEmail(employee.getEmail())) {
			addEmployee(employee);
			return LLMUtils.getResponseEntity("Signed Up Successfully",HttpStatus.OK);
		}
		return LLMUtils.getResponseEntity("Email Already exists",HttpStatus.OK);
	}
	
	public ResponseEntity<String> login(String id, String password){
		Optional<Employee> employeeByID = employeeDAO.findById(id);
		Employee employeeByEmail  = employeeDAO.findByEmailId(id);
		if(!employeeByID.isEmpty()) {
			if(employeeByID.get().getEmployee_id().equals(id)
					&& employeeByID.get().getPassword().equals(password))
				return LLMUtils.getResponseEntity("empid",HttpStatus.OK);
			else
				return LLMUtils.getResponseEntity("empid password",HttpStatus.OK);
		}else if(!Objects.isNull(employeeByEmail)) {
			if(employeeByEmail.getEmail().equals(id)
					&& employeeByEmail.getPassword().equals(password))
			return LLMUtils.getResponseEntity("email",HttpStatus.OK);	
			else
				return LLMUtils.getResponseEntity("email password", HttpStatus.OK);
			
		}		
		return LLMUtils.getResponseEntity("Email or Employee ID doesn't exist", HttpStatus.OK);
	}
	
	public ResponseEntity<Map<String,String>> getEmployeeByEmail(String email) {
		Employee e = employeeDAO.findByEmailId(email);
		Map<String,String> empDetails = new HashMap<String,String>();
		empDetails.put("empID", e.getEmployee_id());
		empDetails.put("name",e.getName());
		empDetails.put("email", e.getEmail());
		empDetails.put("role", e.getRole());
		empDetails.put("status",e.getStatus());
		return new ResponseEntity<Map<String,String>>(empDetails,HttpStatus.OK);
	}
	
	public ResponseEntity<String>updateStatus(Map<String,String>updateDetails){
		String id = updateDetails.get("id");
		String status = updateDetails.get("status");
		Employee employee = employeeDAO.findById(id).get();
		employee.setStatus(status);
		employeeDAO.save(employee);
		String loginStatus = (status.equals("true"))?" can login":" access denied";
		return LLMUtils.getResponseEntity("Employee "+id+" "+loginStatus, HttpStatus.OK);
	}
	
	
	public ResponseEntity<Map<String,String>> getEmployeeByID(String id) {
		Optional<Employee> employee = employeeDAO.findById(id);
		Employee e = employee.get();
		Map<String,String> empDetails = new HashMap<String,String>();
		empDetails.put("empID", e.getEmployee_id());
		empDetails.put("name",e.getName());
		empDetails.put("email", e.getEmail());
		empDetails.put("role", e.getRole());
		empDetails.put("status",e.getStatus());
		return new ResponseEntity<Map<String,String>>(empDetails,HttpStatus.OK);
	}
	
	public ResponseEntity<String> updateEmployee(Map<String,String>updateDetails) {
		
		Employee employee = employeeDAO.findById(updateDetails.get("id")).orElse(null);
		if(!Objects.isNull(employee)) {
			employee.setName(updateDetails.get("name"));
			employee.setEmail(updateDetails.get("email"));
			employee.setRole(updateDetails.get("role"));
			employeeDAO.save(employee);	
			return LLMUtils.getResponseEntity("Employee "+employee.getEmployee_id()+" details updated", HttpStatus.OK);
		}
		return LLMUtils.getResponseEntity("Employee with ID: "+employee.getEmployee_id()+" doesn't exist", HttpStatus.OK);
	}
	
	public ResponseEntity<List<Map<String,String>>> fetchAllEmployees(){
		List<Employee> employees = employeeDAO.findAll();
		
		ArrayList<Map<String,String>> empList = new ArrayList<Map<String,String>>();
		for(Employee emp:employees) {
			Map<String,String> empl = new HashMap<String,String>();
			if(!emp.getRole().equals("Admin")) {
				empl.put("empid", emp.getEmployee_id());
				empl.put("name",emp.getName());
				empl.put("email", emp.getEmail());
				empl.put("role", emp.getRole());
				empl.put("status", emp.getStatus());
				empList.add(empl);				
			}
		}
		return new ResponseEntity<List<Map<String,String>>>(empList,HttpStatus.OK);
	}
	
	public ResponseEntity<String>deleteEmployee(String id){
		Employee employee = employeeDAO.findById(id).orElse(null);
		if(!Objects.isNull(employee)) {
			employeeDAO.delete(employee);
			return LLMUtils.getResponseEntity("Employee "+id+" deleted", HttpStatus.OK);
		}
		return LLMUtils.getResponseEntity("Employee "+ id +" doesn't exist", HttpStatus.OK);
	}
	/**********************************************************************************************/
	
}
