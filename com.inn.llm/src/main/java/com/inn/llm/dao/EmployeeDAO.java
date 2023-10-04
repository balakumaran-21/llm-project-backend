package com.inn.llm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.inn.llm.model.Employee;

public interface EmployeeDAO extends JpaRepository<Employee, Integer>{
	
	Employee findByEmailId(@Param("email") String email);
	
}
