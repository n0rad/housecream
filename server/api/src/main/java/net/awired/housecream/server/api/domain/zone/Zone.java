package net.awired.housecream.server.api.domain.zone;

import javax.activation.MimeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import net.awired.ajsl.persistence.entity.NestedSetEntityImpl;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@XmlRootElement(name = "zone")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlSeeAlso({ Area.class, Building.class, Floor.class, Land.class, Room.class, Field.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = Area.class, name = Area.ZONE_TYPE_NAME),
        @JsonSubTypes.Type(value = Building.class, name = Building.ZONE_TYPE_NAME),
        @JsonSubTypes.Type(value = Floor.class, name = Floor.ZONE_TYPE_NAME),
        @JsonSubTypes.Type(value = Land.class, name = Land.ZONE_TYPE_NAME),
        @JsonSubTypes.Type(value = Room.class, name = Room.ZONE_TYPE_NAME),
        @JsonSubTypes.Type(value = Field.class, name = Field.ZONE_TYPE_NAME) })
public abstract class Zone extends NestedSetEntityImpl<Long> {

    @NotNull
    @Size(min = 1, max = 20)
    @Column(unique = true)
    private String name;

    private MimeType imageMime;
    private byte[] image;

    //    @ElementCollection
    //    private List<Coordinate> coverage;
    //    private List<Coordinate> location;
    //
    //    private List<Point> points;
    //    private Integer ordering;

    @Override
    @XmlElement
    public Long getParentId() {
        return super.getParentId();
    }

    @Override
    public void setParentId(Long parentId) {
        super.setParentId(parentId);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MimeType getImageMime() {
        return imageMime;
    }

    public void setImageMime(MimeType imageMime) {
        this.imageMime = imageMime;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
