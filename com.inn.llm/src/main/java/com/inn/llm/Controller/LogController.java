package com.inn.llm.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inn.llm.model.Log;
import com.inn.llm.service.LogService;

@RestController
@RequestMapping(path = "/log")
@CrossOrigin(origins = "*")
public class LogController {
	@Autowired
	LogService service;
	
	@PostMapping("/addLog")
	public ResponseEntity<String> addLog(@RequestBody String log_entry){
		return service.addLog(log_entry);
	}
	
	@GetMapping("/getLogs/{startDate}/{endDate}")
	public ResponseEntity<List<Log>> getLogs(@PathVariable String startDate, @PathVariable String endDate){
		return service.getLogs(startDate, endDate);
	} 	
	
	
}
