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

import com.inn.llm.model.Device;
import com.inn.llm.service.DeviceService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/device")
public class DeviceController {
	
	@Autowired
	DeviceService service;
	
	public ResponseEntity<String> addDevice(@RequestBody Map<String,String> details){ 
		return service.addDevice(details);
	}
	
	@PostMapping(path = "/addDevices/{num}")
	public ResponseEntity<String> addMoreDevices(@RequestBody Map<String,String> details, @PathVariable int num){
		return service.addDevices(details, num);
	}
	
	@GetMapping("/getDevice/{id}")
	public ResponseEntity<Device> getDevice(@PathVariable String id){
		return service.getDevice(id);
	}
	
	@GetMapping("/getAllDevices")
	public ResponseEntity<List<Device>> getAllDevices(){
		return service.getAllDevices();
	}
	
	@PutMapping("/updateDevice")
	public ResponseEntity<String> updateDevice(@RequestBody Map<String,String> device){
		return service.updateDevice(device);
	}
	
	@DeleteMapping("/deleteDevice/{id}")
	public ResponseEntity<String> deleteDevice(@PathVariable String id){
		return service.deleteDevice(id);
	}
}
