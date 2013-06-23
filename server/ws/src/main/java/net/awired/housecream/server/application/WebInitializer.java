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


//@HandlesTypes(HttpServlet.class)
//public class WebInitializer implements WebApplicationInitializer {
//
//    @Override
//    public void onStartup(ServletContext container) throws ServletException {
//        initHousecream(container);
//        container.addListener(new HousecreamContextLoaderListener(initApplicationContext()));
//        ServletRegistration.Dynamic cxf = container.addServlet("dispatcher", new CXFServlet());
//        cxf.setLoadOnStartup(1);
//        cxf.addMapping("/ws/*");
//    }
//
//    private void initHousecream(ServletContext container) {
//        Housecream.HOUSECREAM.init();
//        Housecream.HOUSECREAM.discoveredVersion(WarManifestUtils.getWarManifestAttribute(container,
//                Housecream.VERSION_MANIFEST_KEY));
//    }
//
//    private WebApplicationContext initApplicationContext() {
//        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
//        context.scan(RootConfig.class.getPackage().getName());
//        context.setDisplayName("Housecream");
//        return context;
//    }
//}
