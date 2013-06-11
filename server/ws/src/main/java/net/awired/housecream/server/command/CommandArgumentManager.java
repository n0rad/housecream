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
package net.awired.housecream.server.command;

import net.awired.aclm.argument.CliArgumentManager;
import net.awired.aclm.argument.CliArgumentParseException;
import net.awired.aclm.argument.args.CliNoParamArgument;
import net.awired.aclm.argument.args.CliOneParamArgument;
import net.awired.aclm.param.CliParamFloat;
import net.awired.aclm.param.CliParamLong;

public class CommandArgumentManager extends CliArgumentManager {

    private final CliNoParamArgument inpoint;
    private final CliNoParamArgument outpoint;
    private final CliNoParamArgument rule;
    private final CliNoParamArgument zone;
    private final CliOneParamArgument<Float> value;
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

        value = new CliOneParamArgument<Float>('v', new CliParamFloat("value"));
        value.setDescription("Set value of an outpoint");
        addArg(value);

        id = new CliOneParamArgument<Long>('0', new CliParamLong("Id"));
        id.setDescription("Display informations about a specific element");
        setDefaultArgument(id);

        zone.addForbiddenArgument(rule);
        zone.addForbiddenArgument(inpoint);
        zone.addForbiddenArgument(outpoint);
        zone.addForbiddenArgument(value);

        rule.addForbiddenArgument(zone);
        rule.addForbiddenArgument(inpoint);
        rule.addForbiddenArgument(outpoint);
        rule.addForbiddenArgument(value);

        inpoint.addForbiddenArgument(zone);
        inpoint.addForbiddenArgument(rule);
        inpoint.addForbiddenArgument(outpoint);
        inpoint.addForbiddenArgument(value);

        outpoint.addForbiddenArgument(zone);
        outpoint.addForbiddenArgument(rule);
        outpoint.addForbiddenArgument(inpoint);
    }

    @Override
    protected void checkParse() throws CliArgumentParseException {
        super.checkParse();
        if (outpoint.isSet() && value.isSet() && !getDefaultArgument().isSet()) {
            throw new CliArgumentParseException("Setting a value require a selected outpoint", value);
        }
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

    public CliOneParamArgument<Float> getValue() {
        return value;
    }

}
