package com.inn.llm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.inn.llm.model.License;

public interface LicenseDAO extends JpaRepository<License, String> {
	
	@Query("select distinct(l.name) from License l")
	public List<String> getLicenseNames();
	
}
