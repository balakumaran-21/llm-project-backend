package com.inn.llm.model;

import java.sql.Date;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
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
	
	@Column(name="name")
	private String name;
	
	@Column(name="type")
	private String type;
	
	@Column(name="date_issued")
	private Date date_issued;
	
	@Column(name="expiry_date")
	private Date expiry_date;
	
	@JsonBackReference
	@OneToOne(optional = true)
	@JoinColumn(name="software_id")
	private Software software;
	
	@JsonBackReference
	@OneToOne(optional = true)
	@JoinColumn(name ="device_id")
	private Device device;
	
}
