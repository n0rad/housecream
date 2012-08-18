package net.awired.housecream.server.it.restmcu;

import net.awired.ajsl.core.lang.exception.UpdateException;
import net.awired.restmcu.api.domain.board.RestMcuBoard;
import net.awired.restmcu.api.resource.client.RestMcuBoardResource;

public class SampleBoardResource implements RestMcuBoardResource {

    private RestMcuBoard board = new RestMcuBoard();

    @Override
    public RestMcuBoard getBoard() {
        return board;
    }

    @Override
    public void setBoard(RestMcuBoard board) throws UpdateException {
        this.board = board;
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
