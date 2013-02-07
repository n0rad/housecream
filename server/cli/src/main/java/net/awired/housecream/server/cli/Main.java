package net.awired.housecream.server.cli;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.ProtectionDomain;
import net.awired.housecream.server.Housecream;
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

        Housecream hc = Housecream.INSTANCE;

        if (argManager.displayFile.isSet()) {
            argManager.displayFile.getParamOneValue().display();
            System.exit(0);
        }

        if (argManager.info.isSet()) {
            hc.printInfo();
            System.exit(0);
        }

        if (argManager.clearDb.isSet()) {
            cleanDb();
            System.exit(0);
        }

        hc.init();
        runServer();
    }

    public void cleanDb() {
        try {
            System.out.println("clearing database in home folder : " + Housecream.INSTANCE.getHome());
            deleteRecursively(new File(Housecream.INSTANCE.getHome(), "db"));
        } catch (IOException e) {
        }
    }

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
        connector.setPort(argManager.portArg.getParamOneValue());
        server.setConnectors(new Connector[] { connector });

        WebAppContext context = new WebAppContext();
        context.setServer(server);
        context.setContextPath(argManager.contextPath.getParamOneValue());

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
