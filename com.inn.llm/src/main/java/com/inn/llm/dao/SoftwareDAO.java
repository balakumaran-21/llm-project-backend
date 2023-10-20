package com.inn.llm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.inn.llm.model.Software;

public interface SoftwareDAO extends JpaRepository<Software, String>{
	@Query("select distinct(s.name) from Software s")
	public List<String> getSoftwareNames();
}
