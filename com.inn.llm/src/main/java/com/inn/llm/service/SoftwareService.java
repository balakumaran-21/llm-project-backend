package com.inn.llm.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.inn.llm.dao.LogDAO;
import com.inn.llm.dao.SoftwareDAO;
import com.inn.llm.model.Device;
import com.inn.llm.model.License;
import com.inn.llm.model.Log;
import com.inn.llm.model.Software;
import com.inn.llm.utils.LLMUtils;

@Service
public class SoftwareService {

	@Autowired
	SoftwareDAO softwareDAO;
	
	@Autowired
	LogDAO logdao;
	
	public ResponseEntity<String> addSoftware(Map<String,String> details){
		Random rand = new Random();
		try {
			Integer id = rand.nextInt(999,9999);
			Software software = new Software();
			software.setSoftware_id(details.get("name").substring(0,3).toUpperCase() + id);
			software.setName(details.get("name"));
			software.setVersion(details.get("version"));
			software.setDate_added(Date.valueOf(details.get("dateAdded")));
			softwareDAO.save(software);
			Log log = new Log();
			log.setLog_entry("A new "+software.getName()+" with id: "+software.getSoftware_id()+" was added");
			logdao.save(log);
			return LLMUtils.getResponseEntity("Software "+software.getSoftware_id()+" added successfully", HttpStatus.OK);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return LLMUtils.getResponseEntity("Somethng went wrong", HttpStatus.OK);
	}
	
	public ResponseEntity<String> addSoftwares(Map<String,String> details, int noOfSoftwares){
		try {
			for(int i= 0; i < noOfSoftwares; i++) {
				addSoftware(details);			
			}
			Log log = new Log();
			log.setLog_entry(noOfSoftwares+" new "+details.get("name")+" was added");
			logdao.save(log);
			return LLMUtils.getResponseEntity("Added "+noOfSoftwares+" new "+details.get("name")+" softwares", HttpStatus.OK);		
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return LLMUtils.getResponseEntity("Something went wrong", HttpStatus.OK);
	}
	
	
	public ResponseEntity<List<Software>> getAllSoftwares(){
		try {			
			return new ResponseEntity<List<Software>>(softwareDAO.findAll(),HttpStatus.OK);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return new ResponseEntity<List<Software>>(new ArrayList<>(),HttpStatus.OK);
	}
	
	public ResponseEntity<Software> getSoftware(String id){
		Software software = softwareDAO.findById(id).orElse(null);
		return new ResponseEntity<Software>(software,HttpStatus.OK);
	}
	
	public ResponseEntity<String> updateSoftware(Map<String, String>details){
		String id = details.get("id");
		try {
			Software software = softwareDAO.findById(id).orElse(null);
			if(Objects.isNull(software)) {
				return LLMUtils.getResponseEntity("ID "+id+" doesn't exist", HttpStatus.OK);
			}
			software.setName(details.get("name"));
			software.setVersion(details.get("version"));
			software.setDate_added(Date.valueOf(details.get("dateAdded")));
			softwareDAO.save(software);			
			Log log = new Log();
			log.setLog_entry(software.getName()+"id: "+software.getSoftware_id()+" details was updated");
			logdao.save(log);
			return LLMUtils.getResponseEntity("Software "+software.getSoftware_id()+" details updated successfully", HttpStatus.OK);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return LLMUtils.getResponseEntity("Something went wrong", HttpStatus.OK);
	}
	
	public ResponseEntity<String> deleteSoftware(String id) {
		
		try {
			Software software = softwareDAO.findById(id).orElse(null);
			if(!Objects.isNull(software)) {
				softwareDAO.deleteById(id);
				Log log = new Log();
				log.setLog_entry(software.getName()+"id: "+software.getSoftware_id()+" was deleted");
				logdao.save(log);
				return LLMUtils.getResponseEntity("Software "+id+" deleted successfully", HttpStatus.OK);
			}
			return LLMUtils.getResponseEntity("Software "+id+" doesn't exist", HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return LLMUtils.getResponseEntity("Something went wrong", HttpStatus.OK);
	}
	
	public ResponseEntity<List<String>> getSoftwareNames() {
		return new ResponseEntity<List<String>>(softwareDAO.getSoftwareNames(),HttpStatus.OK);
	}

	public ResponseEntity<List<Software>> getUnAssignedSoftwares(){
		List<Software> softwareList = new ArrayList<Software>();
		softwareDAO.findAll().forEach((Software software) -> {
			if(Objects.isNull(software.getEmployee())) {
				softwareList.add(software);
			}
		});
		return new ResponseEntity<List<Software>>(softwareList,HttpStatus.OK);
	}
	
	public ResponseEntity<License> getLicense(String id){
		Software software = softwareDAO.findById(id).orElse(null);
		if(!Objects.isNull(software)) {
			return new ResponseEntity<License>(software.getLicense(),HttpStatus.OK);
		}
		return null;
	}
	
	public ResponseEntity<Integer> isLicenseAssigned(String id){
		Software software = softwareDAO.findById(id).orElse(null);
		if(!Objects.isNull(software)) {
			return new ResponseEntity<Integer>(1,HttpStatus.OK);
		}
		return new ResponseEntity<Integer>(0,HttpStatus.OK);
	}
	
}
