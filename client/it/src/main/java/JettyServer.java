//
//
//import net.awired.housecream.client.Server;
//import net.awired.housecream.client.org.eclipse;
//import org.eclipse.jetty.webapp.WebAppContext;
//
//public class JettyServer extends Server {
//
//    org.eclipse.jetty.server.Server server;
//
//    @Override
//    protected void before() throws Throwable {
//        server = new org.eclipse.jetty.server.Server(port);
//        server.setHandler(new WebAppContext("src/main/webapp", "/"));
//        server.start();
//    }
//
//    @Override
//    protected void after() {
//        try {
//            server.stop();
//        } catch (Throwable t) {
//        }
//    }
//}
