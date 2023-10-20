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

import com.inn.llm.model.License;
import com.inn.llm.model.Software;
import com.inn.llm.service.SoftwareService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/software")
public class SoftwareController {

	@Autowired
	SoftwareService service;
	
	@PostMapping("/addSoftware")
	public ResponseEntity<String> addSoftware(@RequestBody Map<String,String> details) {
		return service.addSoftware(details);
	}
	
	@PostMapping("/addSoftwares/{n}")
	public ResponseEntity<String> addSoftwares(@RequestBody Map<String,String> details, @PathVariable int n){
		return service.addSoftwares(details, n);
	}
	
	@GetMapping("/getAllSoftwares")
	public ResponseEntity<List<Software>> getAllSoftwares(){
		return service.getAllSoftwares();
	}
	
	@GetMapping("/getSoftware/{id}")
	public ResponseEntity<Software> getSoftware(@PathVariable String id){
		return service.getSoftware(id);
	}
	
	@PutMapping("/updateSoftware")
	public ResponseEntity<String> updateSoftware(@RequestBody Map<String,String> details){
		return service.updateSoftware(details);
	}
	
	@DeleteMapping("/deleteSoftware/{id}")
	public ResponseEntity<String> deleteSoftware(@PathVariable String id){
		return service.deleteSoftware(id);
	}
	
	@GetMapping("/getSoftwareNames")
	public ResponseEntity<List<String>> getSoftwareNames(){
		return service.getSoftwareNames();
	}
	
	@GetMapping("/getUnAssignedSoftwares")
	public ResponseEntity<List<Software>> getUnAssignedSoftwares(){
		return service.getUnAssignedSoftwares();
	}
	
	@GetMapping("/getLicense/{id}")
	public ResponseEntity<License> getLicense(@PathVariable String id){
		return service.getLicense(id);
	}
}
