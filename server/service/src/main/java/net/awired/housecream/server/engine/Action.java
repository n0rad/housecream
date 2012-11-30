package net.awired.housecream.server.engine;

import java.util.Date;
import net.awired.housecream.server.api.domain.rule.Consequence;
import net.awired.housecream.server.api.domain.rule.TriggerType;

public class Action extends Consequence {

    private Date creation;
    private Integer ruleSource;

    public Action(long outPointId, float value, long delayMili, TriggerType trigger) {
        super(outPointId, value, delayMili, trigger);
    }

    public Action() {
        creation = new Date();

    }

    public Action(ConsequenceAction consequenceAction) {
        this(consequenceAction.getOutPointId(), consequenceAction.getValue(), consequenceAction.getDelayMili(),
                consequenceAction.getTriggerType());
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
