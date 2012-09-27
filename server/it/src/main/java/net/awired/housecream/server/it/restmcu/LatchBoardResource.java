package net.awired.housecream.server.it.restmcu;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import net.awired.ajsl.core.lang.exception.UpdateException;
import net.awired.restmcu.api.domain.board.RestMcuBoard;
import net.awired.restmcu.api.domain.board.RestMcuBoardSettings;
import net.awired.restmcu.api.resource.client.RestMcuBoardResource;

public class LatchBoardResource implements RestMcuBoardResource {

    public final RestMcuBoard board = new RestMcuBoard();
    public final RestMcuBoardSettings boardSettings = new RestMcuBoardSettings();

    private CountDownLatch setLatch = new CountDownLatch(1);

    public void resetLatch() {
        setLatch = new CountDownLatch(1);
    }

    public RestMcuBoardSettings awaitUpdateSettings() throws InterruptedException {
        if (!setLatch.await(10, TimeUnit.SECONDS)) {
            throw new RuntimeException("Countdown timeout");
        }
        return boardSettings;
    }

    @Override
    public RestMcuBoard getBoard() {
        return board;
    }

    @Override
    public RestMcuBoardSettings getBoardSettings() {
        return boardSettings;
    }

    @Override
    public void setBoardSettings(RestMcuBoardSettings boardSettings) throws UpdateException {
        if (boardSettings.getIp() != null) {
            this.boardSettings.setIp(boardSettings.getIp());
        }
        if (boardSettings.getName() != null) {
            this.boardSettings.setName(boardSettings.getName());
        }
        if (boardSettings.getNotifyUrl() != null) {
            this.boardSettings.setNotifyUrl(boardSettings.getNotifyUrl());
        }
        if (boardSettings.getPort() != null) {
            this.boardSettings.setPort(boardSettings.getPort());
        }
        setLatch.countDown();
    }

    @Override
    public void resetBoard() {
    }

    @Override
    public void init() {
    }

    @Override
    public void runNotify() {
    }

}
