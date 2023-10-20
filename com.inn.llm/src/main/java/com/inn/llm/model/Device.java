package com.inn.llm.model;

import java.sql.Date;
import java.time.LocalDate;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
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
@Table(name = "Device")
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
	
	@Column(name = "date_added")
	private Date date_added; 
	
	@JsonBackReference 
	@ManyToOne()
	@JoinColumn(name="employee_id")
	private Employee employee;
	
	@JsonManagedReference
	@OneToOne(mappedBy = "device", cascade = CascadeType.ALL)
    private License license;
	
	@Override
	public String toString() {
		return name+":"+device_id+":"+":"+type+":"+category;  
	}
}
