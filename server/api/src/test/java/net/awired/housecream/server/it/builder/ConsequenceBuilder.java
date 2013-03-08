package net.awired.housecream.server.it.builder;

import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.domain.rule.Consequence;
import net.awired.housecream.server.api.domain.rule.TriggerType;

public class ConsequenceBuilder {

    public static Consequence consequence(OutPoint light, float value) {
        Consequence consequence = new Consequence();
        consequence.setOutPointId(light.getId());
        consequence.setValue(value);
        return consequence;
    }

    public static Consequence consequence(OutPoint light, int value, int delayMs) {
        Consequence consequence = new Consequence();
        consequence.setOutPointId(light.getId());
        consequence.setValue(value);
        consequence.setDelayMili(delayMs);
        return consequence;
    }

    public static Consequence consequence(OutPoint light, int value, int delayMs, TriggerType nonRetrigger) {
        Consequence consequence = new Consequence();
        consequence.setOutPointId(light.getId());
        consequence.setValue(value);
        consequence.setDelayMili(delayMs);
        consequence.setTriggerType(nonRetrigger);
        return consequence;
    }

}
