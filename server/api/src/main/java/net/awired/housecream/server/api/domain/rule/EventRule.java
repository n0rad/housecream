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
    public static final String QUERY_ALL = "QUERY_ALL";

    @NotNull
    @Column(unique = true)
    private String name;

    private Integer salience;

    @Valid
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "RULE_ID", nullable = false)
    private List<Condition> conditions = new ArrayList<Condition>();

    @Valid
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
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

    public Integer getSalience() {
        return salience;
    }

    public void setSalience(Integer salience) {
        this.salience = salience;
    }
}
