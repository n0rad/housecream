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
package org.housecream.server.application;

import static org.housecream.server.Housecream.HOUSECREAM;
import static org.housecream.server.Housecream.VERSION_MANIFEST_KEY;
import static org.housecream.server.application.HousecreamHome.HOUSECREAM_HOME;
import javax.servlet.ServletContextEvent;
import org.springframework.web.context.ContextLoaderListener;
import fr.norad.servlet.sample.html.template.WarManifestUtils;

public class HousecreamContextLoaderListener extends ContextLoaderListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        HOUSECREAM.init();
        HOUSECREAM.discoveredVersion(WarManifestUtils.getWarManifestAttribute(event.getServletContext(),
                VERSION_MANIFEST_KEY));

        HOUSECREAM_HOME.start();
        super.contextInitialized(event);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        HOUSECREAM_HOME.stop();
        super.contextDestroyed(event);
    }
}
