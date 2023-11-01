package com.inn.llm.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.inn.llm.dao.DeviceDAO;
import com.inn.llm.dao.LicenseDAO;
import com.inn.llm.dao.LogDAO;
import com.inn.llm.dao.SoftwareDAO;
import com.inn.llm.model.Device;
import com.inn.llm.model.License;
import com.inn.llm.model.Log;
import com.inn.llm.model.Software;
import com.inn.llm.utils.LLMUtils;

@Service
public class LicenseService {
	@Autowired
	LicenseDAO licenseDAO;
	
	@Autowired
	DeviceDAO deviceDAO;
	
	@Autowired
	SoftwareDAO softwareDAO;
	
	@Autowired
	EmailSenderService mailService;
	
	@Autowired
	LogDAO logdao;
	
	public ResponseEntity<String> addLicense(Map<String,String> details){
		Random random = new Random();
		try {
			License license = new License();
			Integer id = random.nextInt(9999,99999);
			license.setLicense_id(details.get("name").substring(0,4).toUpperCase()+id);
			license.setName(details.get("name"));
			license.setType(details.get("type"));
			license.setDate_issued(Date.valueOf(details.get("dateIssued")));
			license.setExpiry_date(Date.valueOf(details.get("expiryDate")));
			licenseDAO.save(license);
			Log log = new Log();
			log.setLog_entry("A new "+license.getName()+" with id: "+license.getLicense_id()+" was added");
			logdao.save(log);
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
			Log log = new Log();
			log.setLog_entry(noOfLicenses+" new "+details.get("name")+" added");
			logdao.save(log);
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
			license.setName(details.get("name"));
			license.setType(details.get("type"));
			license.setDate_issued(Date.valueOf(details.get("dateIssued")));
			license.setExpiry_date(Date.valueOf(details.get("expiryDate")));
			licenseDAO.save(license);
			Log log = new Log();
			log.setLog_entry(license.getName()+" with id: "+license.getLicense_id()+" details was updated");
			logdao.save(log);
			return LLMUtils.getResponseEntity("License "+id+" details updated", HttpStatus.OK);
		}
		return LLMUtils.getResponseEntity("License "+id+" doesn't exists", HttpStatus.OK);
	}
	
	public ResponseEntity<String> deleteLicense(String id){
		License license = licenseDAO.findById(id).orElse(null);
		if(!Objects.isNull(license)) {
			licenseDAO.deleteById(id);
			Log log = new Log();
			log.setLog_entry(license.getName()+" with id: "+license.getLicense_id()+" was deleted");
			logdao.save(log);
			return LLMUtils.getResponseEntity("License "+id+" deleted", HttpStatus.OK);
		}
		return LLMUtils.getResponseEntity("License "+id+" doesn't exists", HttpStatus.OK);
	}
	
	public ResponseEntity<Device> getDevice(String id){
		License license = licenseDAO.findById(id).orElse(null);
		return new ResponseEntity<Device>(license.getDevice(),HttpStatus.OK);
	}
	
	public ResponseEntity<Software> getSoftware(String id){
		License license = licenseDAO.findById(id).orElse(null);
		return new ResponseEntity<Software>(license.getSoftware(),HttpStatus.OK);
	}
	
	public ResponseEntity<String> assignDevice(Map<String,String> details){
		String dev_id = details.get("dev_id");
		String lic_id = details.get("lic_id");
		License license = licenseDAO.findById(lic_id).orElse(null);
		Device device = deviceDAO.findById(dev_id).orElse(null);
		try {
			if(!Objects.isNull(license) && !Objects.isNull(device)) {
				if(license.getType().toLowerCase().equals("device")) {
					if(Objects.isNull(license.getDevice())) {
						license.setDevice(device);
						licenseDAO.save(license);
						Log log = new Log();
						log.setLog_entry("License "+lic_id+" assigned to "+device.getName()+" id: "+dev_id);
						logdao.save(log);
						return LLMUtils.getResponseEntity("License "+lic_id+" assigned to Device "+dev_id, HttpStatus.OK);						
					}else {
						return LLMUtils.getResponseEntity("License "+license.getLicense_id()+" is already assigned to Device"+license.getDevice().getDevice_id(), HttpStatus.OK);
					}
				}
				else {
					return LLMUtils.getResponseEntity("Can't assign device to this license type", HttpStatus.OK);
				}
			}else if(Objects.isNull(license)) {
				return LLMUtils.getResponseEntity("License id: "+lic_id+" doesn't exist", HttpStatus.OK);
			}else if(Objects.isNull(device)) {
				return LLMUtils.getResponseEntity("Device "+dev_id+" doesn't exist", HttpStatus.OK);
			}			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return LLMUtils.getResponseEntity("Something went wrong", HttpStatus.OK);
	}
	
	public ResponseEntity<List<License>> getUnAssignedLicenses(){
		List<License> list = new ArrayList<License>();
		for(License l:licenseDAO.findAll()) {
			if( Objects.isNull(l.getSoftware()) && Objects.isNull(l.getDevice()) ){
				list.add(l);
			}
		}
		return new ResponseEntity<List<License>>(list,HttpStatus.OK);
	}
	
	public ResponseEntity<String> assignSoftware(Map<String,String> details){
		String lic_id = details.get("lic_id");
		String sof_id = details.get("sof_id");
		License license = licenseDAO.findById(lic_id).orElse(null);
		Software software = softwareDAO.findById(sof_id).orElse(null);
		try {
			if(!Objects.isNull(license) && !Objects.isNull(software)) {
				if(license.getType().toLowerCase().equals("software")) {
					if(Objects.isNull(license.getSoftware())) {
						license.setSoftware(software);
						licenseDAO.save(license);
						Log log = new Log();
						log.setLog_entry("License id: "+lic_id+" assigned to "+software.getName()+" id: "+sof_id);
						logdao.save(log);
						return LLMUtils.getResponseEntity("License "+lic_id+" assigned to software "+sof_id, HttpStatus.OK);						
					}else {
						return LLMUtils.getResponseEntity("License "+license.getLicense_id()+" is already assigned to software"+license.getSoftware().getSoftware_id(), HttpStatus.OK);
					}
				}
				else {
					return LLMUtils.getResponseEntity("Can't assign software to this license type", HttpStatus.OK);
				}
			}else if(Objects.isNull(license)) {
				return LLMUtils.getResponseEntity("License "+lic_id+" doesn't exist", HttpStatus.OK);
			}else if(Objects.isNull(software)) {
				return LLMUtils.getResponseEntity("Software "+sof_id+" doesn't exist", HttpStatus.OK);
			}			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return LLMUtils.getResponseEntity("Something went wrong", HttpStatus.OK);
	}
	
	
	
	public ResponseEntity<List<String>> getLicenseNames() {
		return new ResponseEntity<List<String>>(licenseDAO.getLicenseNames(),HttpStatus.OK);
	}
	
	public ResponseEntity<List<License>> getRequests(){
		List<License> list = licenseDAO.findAll();
		List<License> filterList = new ArrayList<>();
		for(License l:list) {
			if(l.getRequest().equals("yes")) {
				filterList.add(l);
			}
		}
		return new ResponseEntity<List<License>>(filterList,HttpStatus.OK);
	}
	
	public ResponseEntity<String> raiseRequest(String id){
		License l = licenseDAO.findById(id).orElse(null);
		if(!Objects.isNull(l)) {
			Log log = new Log();
			if(l.getRequest().equals("no")) {
				l.setRequest("yes");
				log.setLog_entry("Renewal request raised by "+l.getSoftware().getEmployee().getName()+" for "+l.getName());
				if(l.getType().equals("Software")) {
					if(!Objects.isNull(l.getSoftware()) && !Objects.isNull(l.getSoftware().getEmployee())) {
							String toMail = l.getSoftware().getEmployee().getEmail();
							String name = l.getSoftware().getName();
							String subject = "Renewal request raised";
							String body = "The request for renewal of "+name+" license has been submitted wait for admin's approval";
							mailService.sendEmail(toMail, subject, body);;
					}
				}
				if(l.getType().equals("Device")) {
					if(!Objects.isNull(l.getDevice()) && !Objects.isNull(l.getDevice().getEmployee())) {
							String toMail = l.getDevice().getEmployee().getEmail();
							String name = l.getDevice().getName();
							String subject = "Renewal request raised";
							String body = "The request for renewal of "+name+" license has been submitted wait for admin's approval";
							mailService.sendEmail(toMail, subject, body);;
					}
				}
				licenseDAO.save(l);
				logdao.save(log);
				return LLMUtils.getResponseEntity("Request raised successfully", HttpStatus.OK);
			}
			return LLMUtils.getResponseEntity("Request already raised", HttpStatus.OK);		
		}
		return LLMUtils.getResponseEntity("License doesn't exists", HttpStatus.OK);
	}
	
	public ResponseEntity<String> acceptRequest(String id){
		License l  = licenseDAO.findById(id).orElse(null);
		if(!Objects.isNull(l)) {
			l.setDate_issued(Date.valueOf(LocalDate.now()));
			l.setExpiry_date(Date.valueOf(LocalDate.now().plusYears(1)));
			l.setRequest("no");
			Log log = new Log();
			if(l.getType().equals("Software")) {
				if(!Objects.isNull(l.getSoftware()) && !Objects.isNull(l.getSoftware().getEmployee())) {
						String toMail = l.getSoftware().getEmployee().getEmail();
						String name = l.getSoftware().getName();
						String subject = "License Renewal Request Accepted";
						String body = "The license renewal request for "+name+" software has been accepted and your license has been successfully renewed. "
								+ "The new expiration date is set to one year from now";
						mailService.sendEmail(toMail, subject, body);;
				}
			}
			if(l.getType().equals("Device")) {
				if(!Objects.isNull(l.getDevice()) && !Objects.isNull(l.getDevice().getEmployee())) {
						String toMail = l.getDevice().getEmployee().getEmail();
						String name = l.getDevice().getName();
						String subject = "License Renewal Request Accepted";
						String body = "The license renewal request for "+name+" software has been accepted and your license has been successfully renewed. "
								+ "The new expiration date is set to one year from now";
						mailService.sendEmail(toMail, subject, body);;
				}
			}
			log.setLog_entry("License "+id+" renewed");
			licenseDAO.save(l);
			return LLMUtils.getResponseEntity("Renewal of license successfull", HttpStatus.OK);
		}
		return LLMUtils.getResponseEntity("License doesn't exists", HttpStatus.OK);
	}
	
	@Scheduled(cron = "0 30 21 * * *")
	public void sendLicenseStatus() {
		List<License> licenses = licenseDAO.findAll();
		for(License license:licenses) {
			LocalDate expDate = license.getExpiry_date().toLocalDate();
			LocalDate curr_date = LocalDate.now();
			int diff = Period.between(expDate, curr_date).getDays();
			System.out.println("LICENSE ID: "+license.getLicense_id()+"\n"
					+ "ASSOCIATED NAME: "+ license.getName()+"\n"
					+ "Date Today: "+curr_date.toString()+"\n"
					+ "Expiration Date: "+ expDate.toString()+"\n"
					+ "Days Left: "+ diff+"\n");
			if(diff <= 15) {
				if(license.getType().equals("Software")) {
					if(!Objects.isNull(license.getSoftware()) && !Objects.isNull(license.getSoftware().getEmployee())) {
							String toMail = license.getSoftware().getEmployee().getEmail();
							String name = license.getSoftware().getName();
							String subject = "";
							String body = "";
							if(diff > 0) {
								subject = license.getSoftware().getName() + " license expiring soon...";
								body = "License "+ license.getLicense_id() +" associated with software "+name+" is about to expire in "+diff+" days. "
										+ "Please check status and update as soon as posssible";								
							}else if(diff == 0) {
								subject = license.getSoftware().getName() + "'s license is expiring today";
								body = "License "+ license.getLicense_id() +" associated with software "+name+" is about to expire in "+diff+" days. "
										+ "Please check status and update as soon as posssible";
							}else if(diff < 0) {
								subject = license.getSoftware().getName() + "'s license has expired";
								body = "License "+ license.getLicense_id() +" associated with software "+name+" has expired on "+license.getExpiry_date().toString()+" "
										+ "Please renew license to use "+license.getSoftware().getName();
							}
							mailService.sendEmail(toMail, subject, body);
						}else {
							System.out.println(license.getLicense_id()+": Either license not assigned to any software or software not assigned to any employee");
						}
				}else if(license.getType().equals("Device")) {
					if(!Objects.isNull(license.getDevice()) && !Objects.isNull(license.getDevice().getEmployee())) {
						String toMail = license.getDevice().getEmployee().getEmail();
						String name = license.getDevice().getName();
						String subject = "";
						String body = "";
						if(diff > 0) {
							subject = license.getDevice().getName() + " license expiring soon...";
							body = "License "+ license.getLicense_id() +" associated with device "+name+" is about to expire in "+diff+" days. "
									+ "Please check status and update as soon as posssible";								
						}else if(diff == 0) {
							subject = license.getDevice().getName() + "'s license is expiring today";
							body = "License "+ license.getLicense_id() +" associated with device "+name+" is about to expire in "+diff+" days. "
									+ "Please check status and update as soon as posssible";
						}else if(diff < 0) {
							subject = license.getDevice().getName() + "'s license has expired";
							body = "License "+ license.getLicense_id() +" associated with device "+name+" has expired on "+license.getExpiry_date().toString()+" "
									+ "Please renew license to use "+license.getDevice().getName();
						}
						mailService.sendEmail(toMail, subject, body);
					}else {
						System.out.println(license.getLicense_id()+": Either license not assigned to any device or device not assigned to any employee");
					}
				}				
			}
		}
	}
	
}
