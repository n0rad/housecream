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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import javax.inject.Inject;
import net.awired.aclm.argument.args.CliNoParamArgument;
import net.awired.aclm.argument.args.CliOneParamArgument;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.domain.rule.EventRule;
import net.awired.housecream.server.api.domain.zone.Zone;
import net.awired.housecream.server.engine.OutEvent;
import net.awired.housecream.server.storage.dao.InPointDao;
import net.awired.housecream.server.storage.dao.OutPointDao;
import net.awired.housecream.server.storage.dao.RuleDao;
import net.awired.housecream.server.storage.dao.ZoneDao;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import com.google.common.base.Charsets;

@Component
public class CliProcessor implements Processor {

    private CommandArgumentManager argumentManager = new CommandArgumentManager();

    @Inject
    private InPointDao inPointDao;

    @Inject
    private OutPointDao outPointDao;

    @Inject
    private ZoneDao zoneDao;

    @Inject
    private RuleDao ruleDao;

    @Inject
    private CommandService commandService;

    @Override
    public void process(Exchange exchange) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        argumentManager.setOutputStream(ps);
        argumentManager.setErrorStream(ps);
        try {
            if (argumentManager.parseWithSuccess(exchange.getIn().getBody(String.class).split(" "))) {
                if (argumentManager.getInpoint().isSet()) {
                    processInPoint(argumentManager.getInpoint(), argumentManager.getId(), ps);
                } else if (argumentManager.getOutpoint().isSet()) {
                    processOutPoint(argumentManager.getOutpoint(), argumentManager.getId(),
                            argumentManager.getValue(), ps);
                } else if (argumentManager.getZone().isSet()) {
                    processZone(argumentManager.getZone(), argumentManager.getId(), ps);
                } else if (argumentManager.getRule().isSet()) {
                    processRule(argumentManager.getRule(), argumentManager.getId(), ps);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(ps);
        }
        exchange.getIn().setBody(baos.toString(Charsets.UTF_8.displayName()));
    }

    private void processRule(CliNoParamArgument rule, CliOneParamArgument<Long> id, PrintStream ps)
            throws NotFoundException {
        if (id.isSet()) {
            EventRule find = ruleDao.find(id.getParamOneValue());
            ps.print(find.toString());
        } else {
            List<EventRule> findAll = ruleDao.findAll();
            ps.print(findAll.toString());
        }
    }

    private void processZone(CliNoParamArgument zone, CliOneParamArgument<Long> id, PrintStream ps)
            throws NotFoundException {
        if (id.isSet()) {
            Zone find = zoneDao.find(id.getParamOneValue());
            ps.print(find.toString());
        } else {
            List<Zone> findAll = zoneDao.findAll();
            ps.print(findAll.toString());
        }
    }

    private void processInPoint(CliNoParamArgument inpoint, CliOneParamArgument<Long> id, PrintStream ps)
            throws NotFoundException {
        if (id.isSet()) {
            InPoint find = inPointDao.find(id.getParamOneValue());
            ps.print(find.toString());
        } else {
            List<InPoint> findAll = inPointDao.findAll();
            ps.print(findAll.toString());
        }
    }

    private void processOutPoint(CliNoParamArgument outpoint, CliOneParamArgument<Long> id,
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
                OutPoint find = outPointDao.find(id.getParamOneValue());
                ps.print(find.toString());
            }
        } else {
            List<OutPoint> findAll = outPointDao.findAll();
            ps.print(findAll.toString());
        }
    }
}
