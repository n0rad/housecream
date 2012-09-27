package net.awired.housecream.server.it;

import static net.awired.restmcu.api.domain.line.RestMcuLineNotifyCondition.INF_OR_EQUAL;
import static net.awired.restmcu.api.domain.line.RestMcuLineNotifyCondition.SUP_OR_EQUAL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.awired.ajsl.web.rest.RestContext;
import net.awired.housecream.server.it.restmcu.EmulatorBoardResource;
import net.awired.housecream.server.it.restmcu.EmulatorLineResource;
import net.awired.housecream.server.it.restmcu.LatchLineResource.LineInfo;
import net.awired.restmcu.api.domain.line.RestMcuLine;
import net.awired.restmcu.api.domain.line.RestMcuLineDirection;
import net.awired.restmcu.api.domain.line.RestMcuLineNotify;
import net.awired.restmcu.api.domain.line.RestMcuLineSettings;
import net.awired.restmcu.api.domain.line.RestMcuLineType;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.transport.Destination;
import org.apache.cxf.transport.http_jetty.JettyHTTPDestination;
import org.apache.cxf.transport.http_jetty.JettyHTTPServerEngine;
import org.apache.cxf.transport.http_jetty.ServerEngine;

public class RestMcuSample {

    private Server server;

    public RestMcuSample() {
        EmulatorBoardResource board = new EmulatorBoardResource("127.0.0.1", 8976);
        board.board.setDescription("description of the board");
        board.board.setHardware("RestMcu");
        board.board.setLineIds(Arrays.asList(5, 6, 7, 8));
        board.board.setMac("45:45:45:45:45");
        board.board.setVersion("1.0");
        board.boardSettings.setName("board name");
        board.boardSettings.setNotifyUrl("NOTSETYET");

        EmulatorLineResource line = new EmulatorLineResource(board);
        line.lines.put(5, fillPin());

        String listenAddress = "http://" + board.boardSettings.getIp() + ":" + board.boardSettings.getPort();
        server = new RestContext().prepareServer(listenAddress, Arrays.asList(board, line));

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                server.stop();
            }
        });
    }

    public LineInfo fillPin() {
        LineInfo line = new LineInfo();
        line.value = 42;
        line.description = new RestMcuLine();
        line.description.setDescription("pin 42 description");
        line.description.setDirection(RestMcuLineDirection.INPUT);
        line.description.setType(RestMcuLineType.ANALOG);
        line.description.setValueMax(1024f);
        line.description.setValueMin(0f);

        line.settings = new RestMcuLineSettings();
        line.settings.setName("name of pin");
        List<RestMcuLineNotify> notifies = new ArrayList<RestMcuLineNotify>();
        notifies.add(new RestMcuLineNotify(SUP_OR_EQUAL, 42));
        notifies.add(new RestMcuLineNotify(INF_OR_EQUAL, 42));

        line.settings.setNotifies(notifies);

        return line;
    }

    public static void main(String[] args) throws InterruptedException {
        RestMcuSample restMcuSample = new RestMcuSample();

        Destination dest = restMcuSample.server.getDestination();

        JettyHTTPDestination jettyDestination = JettyHTTPDestination.class.cast(dest);
        ServerEngine engine = jettyDestination.getEngine();
        JettyHTTPServerEngine serverEngine = JettyHTTPServerEngine.class.cast(engine);
        org.eclipse.jetty.server.Server httpServer = serverEngine.getServer();

        httpServer.join();
    }
}
