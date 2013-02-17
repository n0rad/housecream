package net.awired.housecream.server.command;

import net.awired.aclm.argument.CliArgumentManager;
import net.awired.aclm.argument.args.CliNoParamArgument;
import net.awired.aclm.argument.args.CliOneParamArgument;
import net.awired.aclm.param.CliParamLong;

public class CommandArgumentManager extends CliArgumentManager {

    private final CliNoParamArgument inpoint;
    private final CliNoParamArgument outpoint;
    private final CliNoParamArgument rule;
    private final CliNoParamArgument zone;
    private final CliOneParamArgument<Long> id;

    protected CommandArgumentManager() {
        super("");

        inpoint = new CliNoParamArgument('i');
        inpoint.setName("inpoint");
        inpoint.setDescription("Display inpoint informations");
        addArg(inpoint);

        outpoint = new CliNoParamArgument('o');
        outpoint.setName("outpoint");
        outpoint.setDescription("Display outpoint informations");
        addArg(outpoint);

        rule = new CliNoParamArgument('r');
        rule.setName("rule");
        rule.setDescription("Display rule informations");
        addArg(rule);

        zone = new CliNoParamArgument('z');
        zone.setName("zone");
        zone.setDescription("Display zone informations");
        addArg(zone);

        id = new CliOneParamArgument<Long>('0', new CliParamLong("Id"));
        id.setDescription("Display informations about a specific element");
        setDefaultArgument(id);

        zone.addForbiddenArgument(rule);
        zone.addForbiddenArgument(inpoint);
        zone.addForbiddenArgument(outpoint);

        rule.addForbiddenArgument(zone);
        rule.addForbiddenArgument(inpoint);
        rule.addForbiddenArgument(outpoint);

        inpoint.addForbiddenArgument(zone);
        inpoint.addForbiddenArgument(rule);
        inpoint.addForbiddenArgument(outpoint);

        outpoint.addForbiddenArgument(zone);
        outpoint.addForbiddenArgument(rule);
        outpoint.addForbiddenArgument(inpoint);
    }

    public CliNoParamArgument getInpoint() {
        return inpoint;
    }

    public CliNoParamArgument getOutpoint() {
        return outpoint;
    }

    public CliNoParamArgument getRule() {
        return rule;
    }

    public CliNoParamArgument getZone() {
        return zone;
    }

    public CliOneParamArgument<Long> getId() {
        return id;
    }

}
