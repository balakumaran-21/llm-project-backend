package com.inn.llm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inn.llm.model.Device;

public interface DeviceDAO extends JpaRepository<Device, String>{

}
