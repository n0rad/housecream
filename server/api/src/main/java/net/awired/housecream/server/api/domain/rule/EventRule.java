package net.awired.housecream.server.api.domain.rule;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import net.awired.ajsl.persistence.entity.IdEntityImpl;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class EventRule extends IdEntityImpl<Long> {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(unique = true)
    private String name;

    @Valid
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "RULE_ID", nullable = false)
    private List<Condition> conditions = new ArrayList<Condition>();

    @Valid
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "RULE_ID", nullable = false)
    private List<Consequence> consequences = new ArrayList<Consequence>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public List<Consequence> getConsequences() {
        return consequences;
    }

    public void setConsequences(List<Consequence> consequences) {
        this.consequences = consequences;
    }
}
