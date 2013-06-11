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
package net.awired.housecream.server.application;

import javax.servlet.ServletContextEvent;
import net.awired.ajsl.web.WarManifestUtils;
import net.awired.housecream.server.Housecream;
import org.springframework.web.context.ContextLoaderListener;

public class HousecreamContextLoaderListener extends ContextLoaderListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        Housecream.INSTANCE.init();

        Housecream.INSTANCE.updateVersion(WarManifestUtils.getWarManifestAttribute(event.getServletContext(),
                Housecream.VERSION_MANIFEST_KEY));

        HousecreamHome.INSTANCE.init();
        super.contextInitialized(event);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        HousecreamHome.INSTANCE.close();
        super.contextDestroyed(event);
    }
}
