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
package net.awired.housecream.server.api.domain.user;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import net.awired.generic.jpa.entity.IdEntityImpl;

@Entity
@XmlRootElement
public class Role extends IdEntityImpl<String> /* implements GrantedAuthority */{

    @XmlTransient
    @ManyToMany(mappedBy = "roles")
    private List<User> users = new ArrayList<User>();

    ////////////////////////////////////

    public Role() {
    }

    public Role(String name) {
        this.id = name;
    }

    public String getAuthority() {
        return "ROLE_" + id;
    }

    ////////////////////////////////////

    @Override
    @XmlElement
    @Column(unique = true)
    public String getId() {
        return super.getId();
    }

    @Override
    public void setId(String id) {
        super.setId(id);
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
