package net.awired.housecream.server.common.domain;

import javax.persistence.criteria.Order;

public class PointAction {

    private Point point;
    private String action; // should contain a point as its may be the output
                           // point,file,mail,nagios,xmpp, 
    private Order order;

}
