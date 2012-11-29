package net.awired.housecream.server.engine;

import java.util.Date;
import net.awired.housecream.server.api.domain.rule.Consequence;

public class Action extends Consequence {

    private Date creation;
    private Integer ruleSource;

    public Action() {

    }

    public Date getCreation() {
        return creation;
    }

    public void setCreation(Date creation) {
        this.creation = creation;
    }

    public Integer getRuleSource() {
        return ruleSource;
    }

    public void setRuleSource(Integer ruleSource) {
        this.ruleSource = ruleSource;
    }
}
