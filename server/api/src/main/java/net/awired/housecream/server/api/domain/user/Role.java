package net.awired.housecream.server.api.domain.user;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import net.awired.ajsl.persistence.entity.IdEntityImpl;
import org.springframework.security.core.GrantedAuthority;

@Entity
@XmlRootElement
public class Role extends IdEntityImpl<String> implements GrantedAuthority {

    @XmlTransient
    @ManyToMany(mappedBy = "roles")
    private List<User> users = new ArrayList<User>();

    ////////////////////////////////////

    public Role() {
    }

    public Role(String name) {
        this.id = name;
    }

    @Override
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
