package net.awired.housecream.server.it.floor;

import net.awired.housecream.server.common.domain.zone.Floor;
import net.awired.housecream.server.common.resource.FloorResource;
import net.awired.housecream.server.it.HcsItServer;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.junit.Test;

public class FloorIT {

    @Test
    public void should_create_floor() throws Exception {
        FloorResource floorResource = JAXRSClientFactory.create(HcsItServer.getUrl(), FloorResource.class);
        // (1) remote GET call to http://bookstore.com/bookstore
        //        Floor floors = floorResource.getFloor(42L);

        Floor floor = new Floor();
        //        floor.setId(43L);
        floor.setName("salutttt");
        floorResource.createFloor(floor);

        System.out.println(floor);
        // (2) no remote call
        //        BookResource subresource = store.getBookSubresource(1);
        // {3} remote GET call to http://bookstore.com/bookstore/1
        //        Book b = subresource.getBook();

        //<?xml version="1.0" encoding="UTF-8" standalone="yes"?><floor><id>43</id><name>salutttt</name></floor>

    }

}
