package com.inn.llm.model;

import java.sql.Date;
import java.time.LocalDate;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "Product")
public class Device {

	static LocalDate currentDate;
	
	@Id
	@Column(name = "device_id")
	private String device_id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "category")
	private String category;
	
	@Column(name = "emp_id")
	private String emp_id;
	
	@Column(name = "date_added")
	private Date date_added; 
	
	@Override
	public String toString() {
		return name+":"+device_id+":"+":"+type+":"+category;  
	}
}
