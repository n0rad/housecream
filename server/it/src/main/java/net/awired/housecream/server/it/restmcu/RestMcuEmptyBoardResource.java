package net.awired.housecream.server.it.restmcu;

import net.awired.ajsl.core.lang.exception.UpdateException;
import net.awired.restmcu.api.domain.board.RestMcuBoard;
import net.awired.restmcu.api.resource.client.RestMcuBoardResource;

public class RestMcuEmptyBoardResource implements RestMcuBoardResource {

    @Override
    public RestMcuBoard getBoard() {
        return null;
    }

    @Override
    public void setBoard(RestMcuBoard board) throws UpdateException {
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
