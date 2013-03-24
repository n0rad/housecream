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
public class Role extends IdEntityImpl<Long> implements GrantedAuthority {

    @Column(unique = true)
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }

    ///////////////////////////////////////////:

    @XmlTransient
    @ManyToMany(mappedBy = "roles")
    private List<User> users = new ArrayList<User>();

    ////////////////////////////////////

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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
