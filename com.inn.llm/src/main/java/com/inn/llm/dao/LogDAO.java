package com.inn.llm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inn.llm.model.Log;

public interface LogDAO extends JpaRepository<Log, Integer>{

}
