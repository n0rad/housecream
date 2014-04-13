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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.UUID;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.housecream.server.api.domain.point.Point;
import org.housecream.server.api.domain.rule.Rule;
import org.housecream.server.engine.OutEvent;
import org.housecream.server.storage.dao.PointDao;
import org.housecream.server.storage.dao.RuleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.google.common.base.Charsets;
import fr.norad.core.lang.exception.NotFoundException;
import fr.norad.typed.command.line.parser.argument.args.CliNoParamArgument;
import fr.norad.typed.command.line.parser.argument.args.CliOneParamArgument;

@Component
public class CliProcessor implements Processor {

    private CommandArgumentManager argumentManager = new CommandArgumentManager();

    @Autowired
    private PointDao pointDao;

    @Autowired
    private RuleDao ruleDao;

    @Autowired
    private CommandService commandService;

    @Override
    public void process(Exchange exchange) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        argumentManager.setOutputStream(ps);
        argumentManager.setErrorStream(ps);
        try {
            if (argumentManager.parseWithSuccess(exchange.getIn().getBody(String.class).split(" "))) {
            } else if (argumentManager.getPoint().isSet()) {
                processOutPoint(argumentManager.getPoint(), argumentManager.getId(),
                        argumentManager.getValue(), ps);
            } else if (argumentManager.getRule().isSet()) {
                processRule(argumentManager.getRule(), argumentManager.getId(), ps);
            }
        } catch (Exception e) {
            e.printStackTrace(ps);
        }
        exchange.getIn().setBody(baos.toString(Charsets.UTF_8.displayName()));
    }

    private void processRule(CliNoParamArgument rule, CliOneParamArgument<UUID> id, PrintStream ps)
            throws NotFoundException {
        if (id.isSet()) {
            Rule find = ruleDao.find(id.getParamOneValue());
            ps.print(find.toString());
        } else {
            List<Rule> findAll = ruleDao.findAll();
            ps.print(findAll.toString());
        }
    }

//    private void processInPoint(CliNoParamArgument inpoint, CliOneParamArgument<UUID> id, PrintStream ps)
//            throws NotFoundException {
//        if (id.isSet()) {
//            Point find = pointDao.find(id.getParamOneValue());
//            ps.print(find.toString());
//        } else {
//            List<Point> findAll = pointDao.findAll();
//            ps.print(findAll.toString());
//        }
//    }

    private void processOutPoint(CliNoParamArgument outpoint, CliOneParamArgument<UUID> id,
                                 CliOneParamArgument<Float> value, PrintStream ps) throws NotFoundException {
        if (id.isSet()) {
            if (value.isSet()) {
                try {
                    commandService.processOutEvent(new OutEvent(id.getParamOneValue(), value.getParamOneValue()));
                    ps.print("Ok");
                } catch (Exception e) {
                    e.printStackTrace(ps);
                }
            } else {
                Point find = pointDao.find(id.getParamOneValue());
                ps.print(find.toString());
            }
        } else {
            List<Point> findAll = pointDao.findAll();
            ps.print(findAll.toString());
        }
    }
}
