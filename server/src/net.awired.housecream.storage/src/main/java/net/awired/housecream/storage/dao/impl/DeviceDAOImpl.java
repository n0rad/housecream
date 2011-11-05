package net.awired.housecream.storage.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.awired.housecream.storage.dao.DeviceDAO;
import net.awired.housecream.storage.entity.Device;

import org.springframework.stereotype.Repository;

@Repository
//@Transactional
public class DeviceDAOImpl implements DeviceDAO {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void saveDevice(Device device) {
		entityManager.persist(device);
	}

}
