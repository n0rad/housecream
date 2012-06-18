package net.awired.housecream.server.common.domain.rule;

import java.util.List;
import net.awired.ajsl.persistence.entity.IdEntityImpl;

public class Rule extends IdEntityImpl<Long> {

    private static final long serialVersionUID = 1L;

    List<Condition> conditions;

    List<Consequence> consequences;
}
