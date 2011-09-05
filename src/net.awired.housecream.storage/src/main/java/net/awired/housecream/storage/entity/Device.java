package net.awired.housecream.storage.entity;

import javax.persistence.Entity;

import net.awired.ajsl.persistence.entity.implementation.abstracts.IdEntityImpl;

@Entity
public class Device extends IdEntityImpl<Long>{

	private static final long serialVersionUID = 1L;

	private String description = "interrupteur 42";
	private String url = "httparduino:http://192.168.42.4/4";
	private DeviceType type;
	private DeviceWay way;
	
	
}
