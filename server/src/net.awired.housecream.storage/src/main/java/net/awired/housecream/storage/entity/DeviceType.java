package net.awired.housecream.storage.entity;

import net.awired.ajsl.persistence.entity.implementation.abstracts.IdEntityImpl;

public class DeviceType extends IdEntityImpl<Long> {

	private static final long serialVersionUID = 1L;

	private String typeName;
	private ValueType values;

}
