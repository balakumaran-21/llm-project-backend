package com.inn.llm.service;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import org.hibernate.query.sqm.tree.update.SqmUpdateStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.inn.llm.dao.EmailsSentDAO;
import com.inn.llm.dao.LogDAO;
import com.inn.llm.model.EmailsSent;
import com.inn.llm.model.Log;

@Service
public class EmailSenderService {
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private EmailsSentDAO emailDAO;
	
	@Autowired
	LogDAO logdao;
	
	public void sendEmail(String toEmail, 
			String subject,
			String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("balakumaran5102@gmail.com");
		message.setTo(toEmail);
		message.setText(body);
		message.setSubject(subject);
		mailSender.send(message);
		
		EmailsSent email = new EmailsSent();
		email.setTo_mail(toEmail);
		email.setSubject(subject);
		email.setBody(body);
		
		emailDAO.save(email);
		Log log = new Log();
		log.setLog_entry("Automated mail regarding "+email.getSubject()+" was sent to "+email.getTo_mail());
		logdao.save(log);
		System.out.println("Mail sent from balakumaran5102@gmail.com to "+toEmail);
	}
}
