package net.awired.housecream.server.cli;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.ProtectionDomain;
import net.awired.housecream.server.core.application.common.ApplicationHelper;
import org.fusesource.jansi.AnsiConsole;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.webapp.WebAppContext;
import com.google.common.io.Files;

public class Main {
    private final ArgumentManager argManager = new ArgumentManager(this);

    public static void main(String[] args) {
        new Main().run(args);
    }

    public void run(String[] args) {
        AnsiConsole.systemInstall();
        argManager.parse(args);

        System.setProperty(ApplicationHelper.HOME_KEY, ApplicationHelper.findHomeDir());
        System.setProperty(ApplicationHelper.LOG_HOUSECREAM_AWIRED, argManager.logLevel.getParamOneValue().toString());
        System.setProperty(ApplicationHelper.LOG_HOUSECREAM_ROOT, argManager.logLevel.getParamOneValue().toString());
        if (argManager.logRootLevel.isSet()) {
            System.setProperty(ApplicationHelper.LOG_HOUSECREAM_ROOT, argManager.logRootLevel.getParamOneValue()
                    .toString());
        }
        ApplicationHelper.changeLogLvl();

        if (argManager.displayFile.isSet()) {
            argManager.displayFile.getParamOneValue().display();
            System.exit(0);
        }

        if (argManager.info.isSet()) {
            printInfo();
            System.exit(0);
        }

        if (argManager.clearDb.isSet()) {
            cleanDB();
            System.exit(0);
        }

        AnsiConsole.systemInstall();

        runServer();
    }

    public void cleanDB() {
        String home = ApplicationHelper.findHomeDir();
        try {
            System.out.println("clearing database in home folder : " + home);
            Files.deleteRecursively(new File(home + "/db"));
        } catch (IOException e) {
        }
        System.exit(0);
    }

    public void printInfo() {
        ApplicationHelper.getVersion(null);
        System.out.println("HouseCream version : " + ApplicationHelper.getVersion());
        System.out.println("HouseCream home : " + ApplicationHelper.findHomeDir());
        // TODO list plugin founds
    }

    public void runServer() {
        final Server server = new Server();
        SocketConnector connector = new SocketConnector();

        // Set some timeout options to make debugging easier.
        connector.setMaxIdleTime(1000 * 60 * 60);
        connector.setSoLingerTime(-1);
        connector.setPort(argManager.portArg.getParamOneValue());
        server.setConnectors(new Connector[] { connector });

        WebAppContext context = new WebAppContext();
        context.setServer(server);
        context.setContextPath(argManager.contextPath.getParamOneValue());

        ProtectionDomain protectionDomain = Main.class.getProtectionDomain();
        URL location = protectionDomain.getCodeSource().getLocation();
        context.setDescriptor(location.toExternalForm() + "/WEB-INF/web.xml");
        context.setWar(location.toExternalForm());

        server.addHandler(context);
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
