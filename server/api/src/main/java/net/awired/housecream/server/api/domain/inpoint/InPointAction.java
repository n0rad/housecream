package net.awired.housecream.server.api.domain.inpoint;

import javax.persistence.Entity;
import net.awired.ajsl.persistence.entity.IdEntityImpl;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;

@Entity
public class InPointAction extends IdEntityImpl<Long> {

    private static final long serialVersionUID = 1L;

    private InPoint inPoint;
    private OutPoint OutPoint;
    private String action; // should contain a point as its may be the output
                           // point,file,mail,nagios,xmpp, 
                           //    private Order order;
}
