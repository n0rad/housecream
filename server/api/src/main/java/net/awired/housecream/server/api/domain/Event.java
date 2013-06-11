package net.awired.housecream.server.api.domain;

import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement
public class Event {
    private long pointId;
    private float value;

    //    private Severity severity; // INFORM, (ERROR, WARNING), ALERT, DYNAMIC... 
}
