package com.inn.llm.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.inn.llm.dao.DeviceDAO;
import com.inn.llm.dao.LogDAO;
import com.inn.llm.model.Device;
import com.inn.llm.model.Employee;
import com.inn.llm.model.License;
import com.inn.llm.model.Log;
import com.inn.llm.model.Software;
import com.inn.llm.utils.LLMUtils;

@Service
public class DeviceService {
	
	@Autowired
	DeviceDAO deviceDAO;
	
	@Autowired
	LogDAO logdao;
	public ResponseEntity<String> addDevice(Map<String,String> details){  
		Random rand = new Random();
		try {
			Integer id = rand.nextInt(999,9999);
			Device device = new Device();
			String date = details.get("dateAdded");
			device.setCategory(details.get("category"));
			device.setName(details.get("name"));
			device.setType(details.get("type"));
			device.setDevice_id( (details.get("type").substring(0, 3).toUpperCase()) + id.toString());
			device.setDate_added(Date.valueOf(date));
			deviceDAO.save(device);
			Log log = new Log();
			log.setLog_entry("A new"+device.getName()+" with id: "+device.getDevice_id()+" added");
			return LLMUtils.getResponseEntity("Product "+id+" added", HttpStatus.OK);			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return LLMUtils.getResponseEntity("Something went wrong", HttpStatus.OK);
	}
	
	public ResponseEntity<String> addDevices(Map<String,String> details, int numberOfDevices){
		int count = 0;
		for(int i =0; i < numberOfDevices;i++) {
			addDevice(details);
			count++;			
		}
		Log log = new Log();
		log.setLog_entry(count +" new "+details.get("name")+"are added");
		return LLMUtils.getResponseEntity("Added "+count+" "+details.get("name")+" devices", HttpStatus.OK);
	}
	
	public ResponseEntity<Device> getDevice(String id){
		Device device = deviceDAO.findById(id).orElse(null);
		return new ResponseEntity<Device>(device,HttpStatus.OK);
	}
	
	public ResponseEntity<List<Device>> getAllDevices(){
		List<Device> deviceList = deviceDAO.findAll();
		return new ResponseEntity<List<Device>>(deviceList,HttpStatus.OK);
	}
	
	public ResponseEntity<String> updateDevice(Map<String,String> details){
		String id = details.get("id");
		Device device = deviceDAO.findById(id).orElse(null);
		if(!Objects.isNull(device)) {
			device.setDevice_id(id);
			device.setName(details.get("name"));
			device.setCategory(details.get("category"));
			device.setType(details.get("type"));
			device.setDate_added(Date.valueOf(details.get("date_added")));
			System.out.println("Date Added: "+device.getDate_added());
			deviceDAO.save(device);
			Log log = new Log();
			log.setLog_entry(device.getName()+"id: "+device.getDevice_id()+" details was updated");
			logdao.save(log);
			return LLMUtils.getResponseEntity("Device "+id+" details updated",HttpStatus.OK);
		}
		return LLMUtils.getResponseEntity("ID doesn't exists",HttpStatus.OK);
	}
	
	public ResponseEntity<String> deleteDevice(String id) {
		Device device = deviceDAO.findById(id).orElse(null);
		if(!Objects.isNull(device)) {
			deviceDAO.deleteById(id);
			Log log = new Log();
			log.setLog_entry(device.getName()+"id: "+id+" was deleted");
			logdao.save(log);
			return LLMUtils.getResponseEntity("Device "+id+" deleted successfully ", HttpStatus.OK);
		}
		return LLMUtils.getResponseEntity("ID doesn't exist", HttpStatus.OK);
	}
	
	public ResponseEntity<List<String>> getDeviceNames() {
		return new ResponseEntity<List<String>>(deviceDAO.getDeviceNames(),HttpStatus.OK);
	}
	
	public ResponseEntity<List<Device>> getUnAssignedDevices(){
		List<Device> devList = new ArrayList<Device>();
		deviceDAO.findAll().forEach((Device device) -> {
			if(Objects.isNull(device.getEmployee())) {
				devList.add(device);
			}
		});
		return new ResponseEntity<List<Device>>(devList,HttpStatus.OK);
	}
	
	public ResponseEntity<License> getLicense(String id){
		Device device = deviceDAO.findById(id).orElse(null);
		if(!Objects.isNull(device)) {
			return new ResponseEntity<License>(device.getLicense(),HttpStatus.OK);
		}
		return null;
	}
}
