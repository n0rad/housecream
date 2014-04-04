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
package org.housecream.server.application.config;


import java.lang.reflect.Field;
import org.housecream.server.api.domain.HcProperties;
import org.housecream.server.storage.dao.HcPropertiesDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
public class PropertiesConfig {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        PropertySourcesPlaceholderConfigurer placeholder = new PropertySourcesPlaceholderConfigurer();
        return placeholder;
    }

    @Bean
    public HcProperties properties(HcPropertiesDao propsDao) {
        HcProperties properties = new HcProperties();
        propsDao.loadConfig(properties);
        Field[] fields = properties.getClass().getDeclaredFields();
        log.info("########### Properties ############");
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                if (field.getName().endsWith("Secret")) {
                    log.info("## " + field.getName() + " = -> SECRET VALUE");
                } else {
                    log.info("## " + field.getName() + " = " + field.get(properties));
                }
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
        log.info("###################################");
        return properties;
    }

}
