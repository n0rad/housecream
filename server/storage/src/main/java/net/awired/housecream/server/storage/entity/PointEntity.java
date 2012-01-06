package net.awired.housecream.server.storage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import net.awired.ajsl.persistence.entity.IdEntity;
import net.awired.housecream.server.common.domain.Point;

@Entity
public class PointEntity extends Point implements IdEntity<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Override
    public Long getId() {
        return super.getId();
    }

    @Column(unique = true)
    @Override
    public String getName() {
        return super.getName();
    }

}
