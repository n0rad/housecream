/**
 *
 *     Copyright (C) Housecream.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package org.housecream.server.api.domain;


import java.lang.reflect.Field;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class HcProperties {

    private boolean securityEnabled = false;
    private int securitySeedLength = 50;
    private int securityDefaultTokenLifetimeSeconds = 60;
    private int securityDefaultRefreshTokenLifetimeSeconds = 60;
    @XmlTransient
    private String securityGlobalSaltSecret; //TODO should be in a file not in DB
    private int securityMaxFailedLoginAttempt = 3;
    private int securityFailedLoginLifetimeSeconds = 120;
    private int securityRandomStringLength = 128;

    public void importMap(Map<String, String> map) {
        for (String key : map.keySet()) {
            setValue(key, map.get(key));
        }
    }

    public HcProperties setValue(String name, String value) {
        try {
            Field field = this.getClass().getDeclaredField(name);
            Class<?> type = field.getType();
            if (type.equals(Integer.class) || type.equals(Integer.TYPE)) {
                field.setInt(this, Integer.parseInt(value));
            } else if (type.equals(Boolean.class) || type.equals(Boolean.TYPE)) {
                field.set(this, Boolean.parseBoolean(value));
            } else {
                field.set(this, value.toString());
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return this;
    }

    public void importProps(HcProperties properties) {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.set(this, field.get(properties));
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Cannot read fields of properties", e);
            }
        }
    }
}
