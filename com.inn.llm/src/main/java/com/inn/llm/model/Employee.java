package com.inn.llm.model;

import java.io.Serializable;
import java.util.Random;


import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;

@NamedQuery(name = "Employee.findByEmailId", query = "select e from Employee as e where e.email=:email")

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "employee")

public class Employee implements Serializable{

	private static Random random = new Random();
	
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@Column(name = "employee_id")
	private int employee_id  = random.nextInt(10000,99999);
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "role")
	private String role;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "status")
	private String status;
	
}
