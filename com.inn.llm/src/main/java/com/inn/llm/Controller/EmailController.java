package com.inn.llm.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inn.llm.dao.EmailsSentDAO;
import com.inn.llm.model.EmailsSent;

@RequestMapping(path="/email")
@RestController
@CrossOrigin(origins="*")
public class EmailController {
	
	@Autowired
	EmailsSentDAO emailDAO;
	
	@GetMapping("/getEmails")
	public ResponseEntity<List<EmailsSent>> getEmails() {
		return new ResponseEntity<List<EmailsSent>>(emailDAO.findAll(),HttpStatus.OK);
	}
	
	@GetMapping("/getEmail/{id}")
	public ResponseEntity<EmailsSent> getEmail(@PathVariable int id){
		return new ResponseEntity<EmailsSent>(emailDAO.findById(id).orElse(null),HttpStatus.OK);
	}
	
}
