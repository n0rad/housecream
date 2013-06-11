/**
 *
 *     Housecream.org project
 *     Copyright (C) Awired.net
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
package net.awired.housecream.server.storage.application;

import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import org.hibernate.cfg.beanvalidation.BeanValidationEventListener;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Component
public class HibernateEventWiring {

    @Inject
    private EntityManagerFactory factory;

    @Inject
    private LocalValidatorFactoryBean validatorFactory;

    @PostConstruct
    public void registerListeners() {
        BeanValidationEventListener listener = new BeanValidationEventListener(validatorFactory, new Properties());

        HibernateEntityManagerFactory hibernateEntityManagerFactory = (HibernateEntityManagerFactory) factory;
        SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) hibernateEntityManagerFactory
                .getSessionFactory();
        EventListenerRegistry registry = sessionFactoryImpl.getServiceRegistry().getService(
                EventListenerRegistry.class);
        registry.setListeners(EventType.PRE_INSERT, listener);
        registry.setListeners(EventType.PRE_UPDATE, listener);
        //        EventListenerGroup<PreInsertEventListener> eventListenerGroup = 
        //        getEventListenerGroup(EventType.PRE_INSERT);
        //        eventListenerGroup.appendListener(listener);
        //        registry.getEventListenerGroup(EventType.PRE_UPDATE).appendListener(listener);

    }
}
