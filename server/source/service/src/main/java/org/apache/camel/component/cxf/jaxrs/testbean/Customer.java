package org.apache.camel.component.cxf.jaxrs.testbean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @version
 */
@XmlRootElement(name = "Customer")
public class Customer {
    private long id;
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
