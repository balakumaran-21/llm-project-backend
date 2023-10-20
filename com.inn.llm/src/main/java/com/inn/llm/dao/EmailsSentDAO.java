package com.inn.llm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inn.llm.model.EmailsSent;

public interface EmailsSentDAO extends JpaRepository<EmailsSent, Integer>{

}
