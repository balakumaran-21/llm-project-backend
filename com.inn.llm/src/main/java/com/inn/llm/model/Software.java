package com.inn.llm.model;

import java.sql.Date;

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

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Table(name = "software")
public class Software {
	
	@Id
	@Column(name = "software_id")
	private String software_id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "version")
	private String version;
	
	@Column(name = "date_added")
	private Date date_added;
	
	@JsonBackReference(value="emp-software")
	@ManyToOne()
	@JoinColumn(name="employee_id")
	private Employee employee;
	
	@JsonManagedReference(value = "software-license")
	@OneToOne(mappedBy = "software", cascade = CascadeType.ALL)
    private License license;
	
	@Override
	public String toString() {
		return "Software [software_id=" + software_id + ", name=" + name + ", version=" + version + ", date_added="
				+ date_added + "]";
	}
	
	
	
	
}
