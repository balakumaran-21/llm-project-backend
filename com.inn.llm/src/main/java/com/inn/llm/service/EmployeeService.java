package com.inn.llm.service;

import java.util.Optional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.inn.llm.dao.DeviceDAO;
import com.inn.llm.dao.EmployeeDAO;
import com.inn.llm.dao.LicenseDAO;
import com.inn.llm.dao.SoftwareDAO;
import com.inn.llm.model.Device;
import com.inn.llm.model.Employee;
import com.inn.llm.model.Software;
import com.inn.llm.utils.LLMUtils;

@Service
public class EmployeeService{
	
	@Autowired
	EmployeeDAO employeeDAO;
	
	@Autowired
	DeviceDAO deviceDAO;
	
	@Autowired
	SoftwareDAO softwareDAO;
	
	@Autowired
	LicenseDAO licenseDAO;
	
	@Autowired
	EmailSenderService sendMails;
	
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
					&& employeeByEmail.getPassword().equals(password)) {
			return LLMUtils.getResponseEntity("email",HttpStatus.OK);}	
			else
				return LLMUtils.getResponseEntity("email password", HttpStatus.OK);
			
		}		
		return LLMUtils.getResponseEntity("Email or Employee ID doesn't exist", HttpStatus.OK);
	}
	
	public ResponseEntity<Map<String,String>> getEmployeeByEmail(String email) {
		Employee e = employeeDAO.findByEmailId(email);
		Map<String,String> empDetails = new HashMap<String,String>();
		empDetails.put("empid", e.getEmployee_id());
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
		empDetails.put("empid", e.getEmployee_id());
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
	
	@Scheduled(cron="0 0 10 * *  *")
	public void sendMail() {
		sendMails.sendEmail("balakumaran.ma@mailinator.com", "Hello", "Test mail sent");
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
	
	public ResponseEntity<List<Device>> getAllDevicesByID(String id){
		Employee employee = employeeDAO.findById(id).orElse(null);
		System.out.println(employee.getDevices());
		return new ResponseEntity<List<Device>>(employee.getDevices(),HttpStatus.OK);
	}
	
	public ResponseEntity<List<Software>> getAllSoftwaresByID(String id){
		Employee employee = employeeDAO.findById(id).orElse(null);
		System.out.print(employee.getSoftwares());
		return new ResponseEntity<List<Software>>(employee.getSoftwares(),HttpStatus.OK);
	}
	
	public ResponseEntity<String> assignDevices(Map<String,String> details){
		String emp_id = details.get("emp_id");
		String dev_id = details.get("dev_id");
		Employee employee = employeeDAO.findById(emp_id).orElse(null);
		Device device = deviceDAO.findById(dev_id).orElse(null);
		try {
			if(!Objects.isNull(employee) && !Objects.isNull(device)) {
				for(Device dev:employee.getDevices()) {
					if(dev.getType().equals(device.getType())) {
						return LLMUtils.getResponseEntity("A "+device.getType()+" already assigned to this employee",HttpStatus.OK);
					}
				}
				if(Objects.isNull(device.getEmployee())) {
					device.setEmployee(employee);
					deviceDAO.save(device);					
					return LLMUtils.getResponseEntity("Device "+dev_id+" assigned to Employee "+emp_id,HttpStatus.OK);
				}else {
					return LLMUtils.getResponseEntity("This "+device.getType()+" is already assigned to "+device.getEmployee().getName(),HttpStatus.OK);
				}
			}
			else if(Objects.isNull(employee)) {
				return LLMUtils.getResponseEntity("Employee "+emp_id+" doesn't exists ",HttpStatus.OK);
			}else if(Objects.isNull(device)) {
				return LLMUtils.getResponseEntity("Device "+dev_id+" doesn't exists ",HttpStatus.OK);
			}			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return LLMUtils.getResponseEntity("Something went wrong",HttpStatus.OK);
	}
	
	public ResponseEntity<String> assignSoftwares(Map<String,String> details){
		String emp_id = details.get("emp_id");
		String sof_id = details.get("sof_id");
		Employee employee = employeeDAO.findById(emp_id).orElse(null);
		Software software = softwareDAO.findById(sof_id).orElse(null);
			if(!Objects.isNull(employee) && !Objects.isNull(software)) {
				for(Software sfware:employee.getSoftwares()) {
					if(sfware.getName().equals(software.getName())) {
						return LLMUtils.getResponseEntity(software.getName()+" is already assigned to this employee",HttpStatus.OK);
					}
				}
				if(Objects.isNull(software.getEmployee())) {
					software.setEmployee(employee);
					softwareDAO.save(software);					
					return LLMUtils.getResponseEntity("Software "+sof_id+" assigned to Employee "+emp_id,HttpStatus.OK);
				}
			}
			else if(Objects.isNull(employee)) {
				return LLMUtils.getResponseEntity("Employee "+emp_id+" doesn't exist",HttpStatus.OK);
			}else if(Objects.isNull(software)) {
				return LLMUtils.getResponseEntity("Software "+sof_id+" doesn't exist",HttpStatus.OK);
			}			
		return new ResponseEntity<String>("Something went wrong",HttpStatus.OK);
	}
	
	public ResponseEntity<Map<String,Integer>> getAllDetails(){
		Map<String,Integer> details = new HashMap<String,Integer>();
		details.put("employee_count",employeeDAO.findAll().size());
		details.put("device_count", deviceDAO.getDeviceNames().size());
		details.put("software_count", softwareDAO.getSoftwareNames().size());
		details.put("license_count", licenseDAO.getLicenseNames().size());
		return new ResponseEntity<Map<String,Integer>>(details,HttpStatus.OK);
	}
	
	
	/**********************************************************************************************/
	
}
