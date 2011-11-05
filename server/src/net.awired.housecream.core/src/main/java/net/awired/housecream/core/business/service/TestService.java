package net.awired.housecream.core.business.service;

import net.awired.housecream.storage.dao.DeviceDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {

	@Autowired
	DeviceDAO deviceDAO;
	
}
