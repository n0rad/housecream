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
package net.awired.housecream.server.component;

import net.awired.housecream.server.application.ApplicationContextProvider;
import net.awired.housecream.server.command.CommandService;
import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;

public class HcWebConsumer extends DefaultConsumer {

    public HcWebConsumer(Endpoint endpoint, Processor processor) {
        super(endpoint, processor);
    }

    @Override
    public void start() throws Exception {
        super.start();
        ApplicationContextProvider.getInstance().getBean(CommandService.class).setConsumer(this);
    }

    @Override
    public void stop() throws Exception {
        ApplicationContextProvider.getInstance().getBean(CommandService.class).setConsumer(null);
        super.stop();
    }

}
