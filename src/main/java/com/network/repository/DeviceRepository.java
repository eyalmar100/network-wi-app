package com.network.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.network.entity.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
	
	Device findById(String deviceId);
	
 	
}
