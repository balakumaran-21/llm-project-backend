package com.inn.llm.schedular;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskSchedular {
static int count = 0;
	@Scheduled(cron = "0 52 22 * * *")
	public void invokeTask() {
		count+=1;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime l = LocalDateTime.now();
		System.out.println(dtf.format(l)+" Task executed ");
	}
}

