/**
 *
 *     Housecream.org project
 *     Copyright (C) Awired.net
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package net.awired.housecream.server.api.domain;

import java.net.URI;
import java.net.URISyntaxException;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import net.awired.ajsl.persistence.entity.IdEntityImpl;
import net.awired.ajsl.persistence.validator.ForeignId;
import com.google.common.base.Objects;

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
    private String uri;

    //TODO    @Min(value = 1, message = "{org.hibernate.validator.constraints.NotEmpty.message}")
    @ForeignId(daoName = "zoneDao")
    private Long zoneId;

    @Transient
    private Float value;

    @Override
    public String toString() {
        return Objects.toStringHelper(this) //
                .add("id", id) //
                .add("description", description) //
                .add("uri", uri) //
                .add("zoneId", zoneId) //
                .add("value", value) //
                .toString();
    }

    //    private Device device;

    ////////////////////////

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

    public void setUri(URI uri) {
        if (uri == null) {
            this.uri = null;
        } else {
            this.uri = uri.toString();
        }
    }

    public URI getUri() {
        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Cannot rebuild uri", e);
        }
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
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
