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
package net.awired.housecream.server.api.validator;

import java.io.Serializable;
import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.ajsl.persistence.dao.ReadDao;
import net.awired.ajsl.persistence.entity.IdEntity;
import net.awired.housecream.server.api.domain.zone.Zone;
import org.springframework.context.ApplicationContext;

public class ZoneParentTypeValidator implements ConstraintValidator<ZoneParentType, Serializable> {

    @Inject
    private ApplicationContext applicationContext;
    private ReadDao<IdEntity<Serializable>, Serializable> dao;
    private Class<? extends Zone> allowedParentType;

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(ZoneParentType constraintAnnotation) {
        try {
            dao = (ReadDao<IdEntity<Serializable>, Serializable>) applicationContext.getBean(constraintAnnotation
                    .daoName());
            allowedParentType = constraintAnnotation.parentType();
        } catch (Exception e) {
            throw new IllegalStateException("cannot found (or invalid) dao named : " + constraintAnnotation.daoName());
        }
    }

    @Override
    public boolean isValid(Serializable id, ConstraintValidatorContext constraintValidatorContext) {
        if (id == null) {
            return true;
        }
        try {
            IdEntity<Serializable> find = dao.find(id);
            return allowedParentType.isInstance(find);
        } catch (NotFoundException e) {
        }
        return false;
    }

}
