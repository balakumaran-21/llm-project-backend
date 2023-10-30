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

import com.inn.llm.dao.LicenseDAO;
import com.inn.llm.model.Device;
import com.inn.llm.model.License;
import com.inn.llm.model.Software;
import com.inn.llm.service.LicenseService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/license")
public class LicenseController {
	@Autowired
	LicenseService service;
	
	@Autowired
	LicenseDAO licenseDAO;
	@PostMapping("/addLicense")
	public ResponseEntity<String> addlicense(@RequestBody Map<String,String> details) {
		return service.addLicense(details);
	}
	
	@PostMapping("/addLicenses/{n}")
	public ResponseEntity<String> addlicenses(@RequestBody Map<String,String> details, @PathVariable int n){
		return service.addLicenses(details, n);
	}
	
	@GetMapping("/getAllLicenses")
	public ResponseEntity<List<License>> getAllLicenses(){
		return service.getAllLicenses();
	}
	
	@GetMapping("/getLicense/{id}")
	public ResponseEntity<License> getLicense(@PathVariable String id){
		return service.getLicense(id);
	}
	
	@PutMapping("/updateLicense")
	public ResponseEntity<String> updateLicense(@RequestBody Map<String,String> details){
		return service.updateLicense(details);
	}
	
	@DeleteMapping("/deleteLicense/{id}")
	public ResponseEntity<String> deletelicense(@PathVariable String id){
		return service.deleteLicense(id);
	}
	
	@PostMapping("/assignSoftware")
	public ResponseEntity<String> assignSoftware(@RequestBody Map<String,String> details){
		return service.assignSoftware(details);
	}
	
	@PostMapping("/assignDevice")
	public ResponseEntity<String> assignDevice(@RequestBody Map<String,String> details){
		return service.assignDevice(details);
	}
	
	@GetMapping("/getSoftware/{id}")
	public ResponseEntity<Software> getSoftware(@PathVariable String id){
		return service.getSoftware(id);
	}
	
	@GetMapping("/getDevice/{id}")
	public ResponseEntity<Device> getDevice(@PathVariable String id){
		return service.getDevice(id);
	}
	
	@GetMapping("/getLicenseNames")
	public ResponseEntity<List<String>> getLicenseNames(){
		return service.getLicenseNames();
	}
	
	@GetMapping("/getUnAssignedLicenses")
	public ResponseEntity<List<License>> getUnAssignedLicenses(){
		return service.getUnAssignedLicenses();
	}
	
}
