package com.inn.llm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.inn.llm.model.Employee;

public interface EmployeeDAO extends JpaRepository<Employee, String>{
	
	@Query("select e from Employee e where e.email=:email")
	Employee findByEmailId( String email);
	
}
