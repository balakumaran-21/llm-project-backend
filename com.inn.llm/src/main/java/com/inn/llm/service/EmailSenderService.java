package com.inn.llm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.inn.llm.dao.EmailsSentDAO;
import com.inn.llm.model.EmailsSent;

@Service
public class EmailSenderService {
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private EmailsSentDAO emailDAO;
	
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
		
		System.out.println("Mail sent from balakumaran5102@gmail.com to "+toEmail);
	}
}
