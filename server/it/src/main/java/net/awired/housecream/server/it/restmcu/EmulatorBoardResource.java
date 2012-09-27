package net.awired.housecream.server.it.restmcu;

public class EmulatorBoardResource extends LatchBoardResource {

    public EmulatorBoardResource(String ip, int port) {
        boardSettings.setIp(ip);
        boardSettings.setPort(port);
    }

}
