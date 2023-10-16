package com.inn.llm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inn.llm.model.License;

public interface LicenseDAO extends JpaRepository<License, String> {

}
