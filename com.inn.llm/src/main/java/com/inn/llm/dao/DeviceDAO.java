package com.inn.llm.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.inn.llm.model.Device;

public interface DeviceDAO extends JpaRepository<Device, String>{
	
	@Query("select distinct(d.name) from Device d")
	public List<String> getDeviceNames();
	
}
