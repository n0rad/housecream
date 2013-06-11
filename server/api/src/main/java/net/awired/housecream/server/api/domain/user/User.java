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
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import net.awired.ajsl.persistence.entity.IdEntityImpl;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

@Entity
//@NamedQueries({ //
//@NamedQuery(name = QUERY_BY_USERNAME, query = "SELECT u FROM User u WHERE u.username = :" + QUERY_PARAM_USERNAME) })
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class User extends IdEntityImpl<String> /* implements UserDetails */{

    public static final String QUERY_PARAM_USERNAME = "QUERY_PARAM_USERNAME";
    public static final String QUERY_BY_USERNAME = "QUERY_BY_USERNAME";

    @XmlTransient
    private String hashedPassword;

    @Transient
    @Length(min = 8)
    private String clearPassword;

    private Date passwordExpiration;
    private Date accountExpiration;
    private boolean enabled = true;
    private boolean locked = false;

    @Email
    private String email;

    //////////////////////////////////////

    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "roleId"))
    private List<Role> roles = new ArrayList<Role>();

    //////////////////////////////////////

    public User() {
    }

    public User(String id, String password) {
        this.id = id;
        this.clearPassword = password;
    }

    //    public Collection<? extends GrantedAuthority> getAuthorities() {
    //        return null;
    //        //        return groups;
    //    }

    @XmlTransient
    public boolean isAccountNonExpired() {
        if (passwordExpiration == null) {
            return true;
        }
        return new Date().getTime() - passwordExpiration.getTime() > 0;
    }

    @XmlTransient
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @XmlTransient
    public boolean isCredentialsNonExpired() {
        if (passwordExpiration == null) {
            return true;
        }
        return new Date().getTime() - passwordExpiration.getTime() > 0;
    }

    public String getPassword() {
        return hashedPassword;
    }

    public String getUsername() {
        return id;
    }

    //////////////////////////////////////

    @Length(min = 5)
    @Column(unique = true)
    @Override
    @XmlElement
    public String getId() {
        return super.getId();
    }

    @Override
    public void setId(String id) {
        super.setId(id);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Date getPasswordExpiration() {
        return passwordExpiration;
    }

    public void setPasswordExpiration(Date passwordExpiration) {
        this.passwordExpiration = passwordExpiration;
    }

    public Date getAccountExpiration() {
        return accountExpiration;
    }

    public void setAccountExpiration(Date accountExpiration) {
        this.accountExpiration = accountExpiration;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getClearPassword() {
        return clearPassword;
    }

    public void setClearPassword(String clearPassword) {
        this.clearPassword = clearPassword;
    }

}
