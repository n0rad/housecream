package net.awired.housecream.server.common.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import net.awired.ajsl.persistence.entity.IdEntityImpl;
import net.awired.housecream.server.common.domain.inpoint.InPointType;

/**
 * A capture or command point (temperature, switch, ...)
 */
//@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@MappedSuperclass
public abstract class Point extends IdEntityImpl<Long> {

    private static final long serialVersionUID = 1L;

    //    private Coordinate position;
    //    private List<Coordinate> coverage;

    @NotNull
    @Column(unique = true)
    private String name; // = "Interrupteur 42";
    //    private String description = "Eclairage principal de la piece";

    @NotNull
    private String url; // = "hcc:http://192.168.42.4/pin/4";

    //    private PointDirection direction;

    //    private Device device;

    ////////////////////////

    //    private Area area;

    ////////////////////

    public InPointType getPointComponentType() {
        return null; // hcc, x10, xmpp, mail, http, ...
    }

    @Override
    @XmlElement
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }

    ///////////////////////////

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

}
