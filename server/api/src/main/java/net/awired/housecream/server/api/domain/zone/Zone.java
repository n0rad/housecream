package net.awired.housecream.server.api.domain.zone;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import net.awired.ajsl.persistence.entity.NestedSetEntityImpl;
import net.awired.housecream.server.api.domain.CoordinateShape;
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
    private String name;

    private String imageMime;
    @Lob
    private byte[] image;

    private CoordinateShape parentZoneCoordinatesShape;
    private String parentZoneCoordinates;

    // TODO   private Integer ordering;

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

    public String getImageMime() {
        return imageMime;
    }

    public void setImageMime(String imageMime) {
        this.imageMime = imageMime;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public CoordinateShape getParentZoneCoordinatesShape() {
        return parentZoneCoordinatesShape;
    }

    public void setParentZoneCoordinatesShape(CoordinateShape parentZoneCoordinatesShape) {
        this.parentZoneCoordinatesShape = parentZoneCoordinatesShape;
    }

    public String getParentZoneCoordinates() {
        return parentZoneCoordinates;
    }

    public void setParentZoneCoordinates(String parentZoneCoordinates) {
        this.parentZoneCoordinates = parentZoneCoordinates;
    }
}
