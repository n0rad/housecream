package net.awired.housecream.server.api.domain.user;

import static net.awired.housecream.server.api.domain.user.User.QUERY_BY_USERNAME;
import static net.awired.housecream.server.api.domain.user.User.QUERY_PARAM_USERNAME;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import net.awired.ajsl.persistence.entity.IdEntityImpl;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@NamedQueries({ //
@NamedQuery(name = QUERY_BY_USERNAME, query = "SELECT u FROM User u WHERE u.username = :" + QUERY_PARAM_USERNAME) })
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class User extends IdEntityImpl<Long> implements UserDetails {

    public static final String QUERY_PARAM_USERNAME = "QUERY_PARAM_USERNAME";
    public static final String QUERY_BY_USERNAME = "QUERY_BY_USERNAME";

    @Length(min = 5)
    @Column(unique = true)
    private String username;

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

    public User(String username, String password) {
        this.username = username;
        this.clearPassword = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
        //        return groups;
    }

    @Override
    @XmlTransient
    public boolean isAccountNonExpired() {
        if (passwordExpiration == null) {
            return true;
        }
        return new Date().getTime() - passwordExpiration.getTime() > 0;
    }

    @Override
    @XmlTransient
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    @XmlTransient
    public boolean isCredentialsNonExpired() {
        if (passwordExpiration == null) {
            return true;
        }
        return new Date().getTime() - passwordExpiration.getTime() > 0;
    }

    @Override
    public String getPassword() {
        return hashedPassword;
    }

    //////////////////////////////////////

    @Override
    @XmlElement
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
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
