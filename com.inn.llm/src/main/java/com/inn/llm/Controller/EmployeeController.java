package com.inn.llm.Controller;

import java.util.List;

import java.util.Map;
import java.util.Random;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inn.llm.dao.EmployeeDAO;
import com.inn.llm.model.Device;
import com.inn.llm.model.Employee;
import com.inn.llm.model.Software;
import com.inn.llm.service.EmployeeService;

@RestController
@CrossOrigin(origins= "*")
@RequestMapping(path = "/employee")
public class EmployeeController{

	@Autowired
	EmployeeDAO employeedao;
	
	@Autowired
	EmployeeService service;
	
	@Autowired
	JavaMailSender mailsender;
	
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
		
	@GetMapping("/getDevices/{id}")
	public ResponseEntity<List<Device>> getDevices(@PathVariable String id){
		return service.getAllDevicesByID(id);
	}
	
	@PostMapping("/assignDevices")
	public ResponseEntity<String> assignDevices(@RequestBody Map<String,String> details){
		return service.assignDevices(details);
	}
	
	@PostMapping("/assignSoftwares")
	public ResponseEntity<String> assignSoftwares(@RequestBody Map<String,String> details){
		return service.assignSoftwares(details);
	}
	
	@GetMapping("/getSoftwares/{id}")
	public ResponseEntity<List<Software>> getSoftwares(@PathVariable String id){
		return service.getAllSoftwaresByID(id);
	}
	
	@GetMapping("/getAllDetails")
	public ResponseEntity<Map<String, Integer>> getDetails(){
		return service.getAllDetails();
	}
	
	@GetMapping(path="/forgotpassword/{email1}")
	public String forgotpassword(@PathVariable("email1") String email)
	{
		System.out.println(email + "helo by email");
		Employee employee=employeedao.findByEmailId(email);
		int otp=sendEmail(email);
		return String.valueOf(otp);
	}
	
	@PutMapping(path="updatepassword/{email}/{password}")
	public String updatepassword(@PathVariable("email") String email,@PathVariable("password") String password)
	{
		Employee employee=employeedao.findByEmailId(email);
		employee.setPassword(password);
		employeedao.save(employee);
		return JSONObject.quote("Updated");
	}
	
	public int sendEmail(String toEmail) {
		SimpleMailMessage message=new SimpleMailMessage();
		message.setTo(toEmail);
		Random rand=new Random();
		int x=111111;
		int y=999999;
		int z=rand.nextInt(y-x+1)+x;
		message.setText(String.valueOf(z));
		message.setSubject("Otp for your Login process");
		message.setFrom("travelstour40@gmail.com");
		mailsender.send(message);
		return z;
	}
	
}
