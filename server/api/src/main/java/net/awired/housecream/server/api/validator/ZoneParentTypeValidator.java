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
