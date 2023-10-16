package com.inn.llm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inn.llm.model.Software;

public interface SoftwareDAO extends JpaRepository<Software, String>{
	
}
