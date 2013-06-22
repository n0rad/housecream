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
package net.awired.housecream.server.cli;

import static net.awired.housecream.server.Housecream.HOUSECREAM;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.ProtectionDomain;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.webapp.WebAppContext;

public class Main {
    private final ArgumentManager argManager = new ArgumentManager();

    public static void main(String[] args) {
        new Main().run(args);
    }

    public void run(String[] args) {
        if (!argManager.parseWithSuccess(args)) {
            return;
        }

        if (argManager.getDisplayFile().isSet()) {
            argManager.getDisplayFile().getParamOneValue().display();
            System.exit(0);
        }

        if (argManager.getInfo().isSet()) {
            System.out.println("Housecream version          : " + HOUSECREAM.getVersion());
            System.out.println("Housecream home             : " + HOUSECREAM.getHome());
            System.out.println("Housecream log conf         : " + HOUSECREAM.getLogbackConf());
            System.out.println("Housecream plugin directory : " + HOUSECREAM.getPluginDirectory());
            System.exit(0);
        }

        //        if (argManager.getClearDb().isSet()) {
        //            clearDb();
        //            System.exit(0);
        //        }

        HOUSECREAM.init();
        runServer();
    }

    //    public void clearDb() {
    //        try {
    //            System.out.println("Clearing database in home folder : " + HOUSECREAM.getHome());
    //            deleteRecursively(new File(HOUSECREAM.getHome(), "db"));
    //        } catch (IOException e) {
    //        }
    //    }

    public void deleteRecursively(File directory) throws IOException {
        // Symbolic links will have different canonical and absolute paths
        if (!directory.getCanonicalPath().equals(directory.getAbsolutePath())) {
            return;
        }
        File[] files = directory.listFiles();
        if (files == null) {
            throw new IOException("Error listing files for " + directory);
        }
        for (File file : files) {
            deleteRecursively(file);
        }
    }

    public void runServer() {
        final Server server = new Server();
        SocketConnector connector = new SocketConnector();

        // Set some timeout options to make debugging easier.
        connector.setMaxIdleTime(1000 * 60 * 60);
        connector.setSoLingerTime(-1);
        connector.setPort(argManager.getHousecreamPort().getParamOneValue());
        server.setConnectors(new Connector[] { connector });

        WebAppContext context = new WebAppContext();
        context.setServer(server);
        context.setContextPath(argManager.getHousecreamContextPath().getParamOneValue());

        ProtectionDomain protectionDomain = Main.class.getProtectionDomain();
        URL location = protectionDomain.getCodeSource().getLocation();
        context.setDescriptor(location.toExternalForm() + "/WEB-INF/web.xml");
        context.setWar(location.toExternalForm());

        server.setHandler(context);
        try {
            server.start();
            server.setStopAtShutdown(true);
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(100);
        }
    }
}
