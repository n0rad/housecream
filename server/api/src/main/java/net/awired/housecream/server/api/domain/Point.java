package net.awired.housecream.server.api.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import net.awired.ajsl.persistence.entity.IdEntityImpl;
import net.awired.ajsl.persistence.validator.ForeignId;

@MappedSuperclass
@XmlAccessorType(XmlAccessType.PROPERTY)
public abstract class Point extends IdEntityImpl<Long> {

    private static final long serialVersionUID = 1L;

    //    private Coordinate position;
    //    private List<Coordinate> coverage;

    @NotNull
    @Size(min = 1, max = 20)
    @Column(unique = true)
    private String name;

    @Column
    @Size(min = 0, max = 100)
    private String description;

    @NotNull
    @Column(unique = true)
    private String url;

    @Min(value = 1, message = "{org.hibernate.validator.constraints.NotEmpty.message}")
    @ForeignId(daoName = "zoneDao")
    private long zoneId;

    @Transient
    private Float value;

    //    private Device device;

    ////////////////////////

    public String extractUrlPrefix() {
        if (url == null) {
            return null;
        }
        int indexOf = url.indexOf(':');
        if (indexOf == -1) {
            return null;
        }
        return url.substring(0, indexOf);
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

    public long getZoneId() {
        return zoneId;
    }

    public void setZoneId(long zoneId) {
        this.zoneId = zoneId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

}
