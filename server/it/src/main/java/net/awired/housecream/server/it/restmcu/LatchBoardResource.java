package net.awired.housecream.server.it.restmcu;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import net.awired.ajsl.core.lang.exception.UpdateException;
import net.awired.restmcu.api.domain.board.RestMcuBoard;
import net.awired.restmcu.api.domain.board.RestMcuBoardSettings;
import net.awired.restmcu.api.resource.client.RestMcuBoardResource;

public class LatchBoardResource implements RestMcuBoardResource {

    private RestMcuBoard board = new RestMcuBoard();
    private RestMcuBoardSettings boardSettings = new RestMcuBoardSettings();

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
        this.boardSettings = boardSettings;
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
