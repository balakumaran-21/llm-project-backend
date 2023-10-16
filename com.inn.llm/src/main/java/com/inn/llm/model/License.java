package com.inn.llm.model;

import java.sql.Date;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Table(name = "license")
public class License {
	@Id
	@Column(name="license_id")
	private String license_id;
	
	@Column(name="assoc_id")
	private String assoc_id;
	
	@Column(name="type")
	private String type;
	
	@Column(name="date_issued")
	private Date date_issued;
	
	@Column(name="expiry_date")
	private Date expiry_date;
}
