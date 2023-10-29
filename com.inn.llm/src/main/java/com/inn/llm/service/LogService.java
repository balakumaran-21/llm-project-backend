package com.inn.llm.service;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.inn.llm.dao.LogDAO;
import com.inn.llm.model.Log;
import com.inn.llm.utils.LLMUtils;

@Service
public class LogService {
	@Autowired
	LogDAO logDAO;
	
	public ResponseEntity<String> addLog(String log_entry) {
		Log log = new Log();
		log.setLog_entry(log_entry);
//		log.setDate();
//		log.setTime();
		logDAO.save(log);
		return LLMUtils.getResponseEntity("log entry successfull", HttpStatus.OK);
	}
	
	public ResponseEntity<List<Log>> getLogs(String startDate, String endDate){
		List<Log> list = new ArrayList<Log>();
		Date sDate = Date.valueOf(startDate);
		Date eDate = Date.valueOf(endDate);
		for(Log l:logDAO.findAll()) {
			if(l.getDate().compareTo(sDate) == 0 || l.getDate().after(sDate) && l.getDate().before(eDate) || l.getDate().compareTo(eDate) == 0) {
				list.add(l);
			}
		}
		return new ResponseEntity<List<Log>>(list,HttpStatus.OK); 
	}
	
}
