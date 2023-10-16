package com.inn.llm.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.inn.llm.dao.LicenseDAO;
import com.inn.llm.model.License;
import com.inn.llm.utils.LLMUtils;

@Service
public class LicenseService {
	@Autowired
	LicenseDAO licenseDAO;
	
	public ResponseEntity<String> addLicense(Map<String,String> details){
		Random random = new Random();
		try {
			License license = new License();
			Integer id = random.nextInt(9999,99999);
			license.setLicense_id(details.get("type").substring(0,4).toUpperCase()+id);
			license.setType(details.get("type"));
			license.setDate_issued(Date.valueOf(details.get("dateIssued")));
			license.setExpiry_date(Date.valueOf(details.get("expiryDate")));
			licenseDAO.save(license);
			return LLMUtils.getResponseEntity("Added new "+id+" license", HttpStatus.OK);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return LLMUtils.getResponseEntity("Something went wrong", HttpStatus.OK);
	}
	
	public ResponseEntity<String> addLicenses(Map<String,String> details, int noOfLicenses){
		try {
			for(int i = 0; i < noOfLicenses; i++) {
				addLicense(details);
			}
			return LLMUtils.getResponseEntity("Added "+noOfLicenses+" new "+details.get("type")+" licenses", HttpStatus.OK);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return LLMUtils.getResponseEntity("Something went wrong", HttpStatus.OK);
	}
	
	public ResponseEntity<License> getLicense(String id){
		License license = licenseDAO.findById(id).orElse(null);
		return new ResponseEntity<License>(license,HttpStatus.OK);
	}
	
	public ResponseEntity<List<License>> getAllLicenses(){
		return new ResponseEntity<List<License>> (licenseDAO.findAll(),HttpStatus.OK);
	}
	
	public ResponseEntity<String> updateLicense(Map<String,String> details){
		String id = details.get("id");
		License license = licenseDAO.findById(id).orElse(null);
		if(!Objects.isNull(license)) {
			license.setType(details.get("type"));
			license.setDate_issued(Date.valueOf(details.get("dateIssued")));
			license.setExpiry_date(Date.valueOf(details.get("expiryDate")));
			licenseDAO.save(license);
			return LLMUtils.getResponseEntity("License "+id+" details updated", HttpStatus.OK);
		}
		return LLMUtils.getResponseEntity("License "+id+" doesn't exists", HttpStatus.OK);
	}
	
	public ResponseEntity<String> deleteLicense(String id){
		License license = licenseDAO.findById(id).orElse(null);
		if(!Objects.isNull(license)) {
			licenseDAO.deleteById(id);
			return LLMUtils.getResponseEntity("License "+id+" deleted", HttpStatus.OK);
		}
		return LLMUtils.getResponseEntity("License "+id+" doesn't exists", HttpStatus.OK);
	}
	
}
