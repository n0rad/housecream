package net.awired.housecream.server.it.restmcu;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import net.awired.ajsl.core.lang.exception.UpdateException;
import net.awired.restmcu.api.domain.board.RestMcuBoard;
import net.awired.restmcu.api.resource.client.RestMcuBoardResource;

public class LatchBoardResource implements RestMcuBoardResource {

    private RestMcuBoard board = new RestMcuBoard();

    private CountDownLatch setLatch = new CountDownLatch(1);

    public void resetLatch() {
        setLatch = new CountDownLatch(1);
    }

    public RestMcuBoard awaitSet() throws InterruptedException {
        setLatch.await(10, TimeUnit.SECONDS);
        return board;
    }

    @Override
    public RestMcuBoard getBoard() {
        return board;
    }

    @Override
    public void setBoard(RestMcuBoard board) throws UpdateException {
        this.board = board;
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
