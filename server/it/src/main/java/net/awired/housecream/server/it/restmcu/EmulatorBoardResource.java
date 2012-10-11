package net.awired.housecream.server.it.restmcu;

import net.awired.housecream.camel.restmcu.LatchBoardResource;

public class EmulatorBoardResource extends LatchBoardResource {

    public EmulatorBoardResource(String ip, int port) {
        boardSettings.setIp(ip);
        boardSettings.setPort(port);
    }

}
