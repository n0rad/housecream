package net.awired.housecream.server.engine;

import net.awired.housecream.server.api.domain.rule.TriggerType;

public class ConsequenceAction extends Action {

    public ConsequenceAction(long outPointId, float value, long delayMili, TriggerType trigger) {
        super(outPointId, value, delayMili, trigger);
    }

}
