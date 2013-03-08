package net.awired.housecream.server.it.builder;

import net.awired.housecream.server.api.domain.Point;
import net.awired.housecream.server.api.domain.rule.Condition;
import net.awired.housecream.server.api.domain.rule.ConditionType;

public class ConditionBuilder {

    public static ConditionBuilder condition() {
        return new ConditionBuilder();
    }

    public static Condition condition(Point point, float value, ConditionType type) {
        Condition condition = new Condition();
        condition.setValue(value);
        condition.setType(type);
        condition.setPointId(point.getId());
        return condition;
    }
}
