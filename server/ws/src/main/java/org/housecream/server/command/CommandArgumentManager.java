/**
 *
 *     Copyright (C) Housecream.org
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
package org.housecream.server.command;

import java.util.UUID;
import fr.norad.typed.command.line.parser.argument.CliArgumentManager;
import fr.norad.typed.command.line.parser.argument.CliArgumentParseException;
import fr.norad.typed.command.line.parser.argument.args.CliNoParamArgument;
import fr.norad.typed.command.line.parser.argument.args.CliOneParamArgument;
import fr.norad.typed.command.line.parser.param.CliParamFloat;
import fr.norad.typed.command.line.parser.param.CliParamUuid;

public class CommandArgumentManager extends CliArgumentManager {

    private final CliNoParamArgument point;
    private final CliNoParamArgument rule;
    private final CliOneParamArgument<Float> value;
    private final CliOneParamArgument<UUID> id;

    protected CommandArgumentManager() {
        super("");

        point = new CliNoParamArgument('p');
        point.setName("inpoint");
        point.setDescription("Display point informations");
        addArg(point);

        rule = new CliNoParamArgument('r');
        rule.setName("rule");
        rule.setDescription("Display rule informations");
        addArg(rule);

        value = new CliOneParamArgument<>('v', new CliParamFloat("value"));
        value.setDescription("Set value of an outpoint");
        addArg(value);

        id = new CliOneParamArgument<>('0', new CliParamUuid("Id"));
        id.setDescription("Display informations about a specific element");
        setDefaultArgument(id);

        rule.addForbiddenArgument(point);
        rule.addForbiddenArgument(value);

        point.addForbiddenArgument(rule);
        point.addForbiddenArgument(value);
    }

    @Override
    protected void checkParse() throws CliArgumentParseException {
        super.checkParse();
        if (point.isSet() && value.isSet() && !getDefaultArgument().isSet()) {
            throw new CliArgumentParseException("Setting a value require a selected point", value);
        }
    }

    public CliNoParamArgument getPoint() {
        return point;
    }

    public CliNoParamArgument getRule() {
        return rule;
    }

    public CliOneParamArgument<UUID> getId() {
        return id;
    }

    public CliOneParamArgument<Float> getValue() {
        return value;
    }

}
