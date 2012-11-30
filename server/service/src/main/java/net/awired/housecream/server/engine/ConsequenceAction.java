package net.awired.housecream.server.engine;

import net.awired.housecream.server.api.domain.rule.Consequence;
import net.awired.housecream.server.api.domain.rule.TriggerType;

public class ConsequenceAction extends Consequence {

    public ConsequenceAction(long outPointId, float value, long delayMili, TriggerType trigger) {
        super(outPointId, value, delayMili, trigger);
    }

}
