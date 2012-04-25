package net.awired.housecream.client;

import net.awired.housecream.client.common.domain.board.HccBoard;
import net.awired.housecream.client.common.resource.HccUpdateException;
import net.awired.housecream.client.common.resource.client.HccBoardResource;

public class BoardResource implements HccBoardResource {
    @Override
    public HccBoard getBoard() {
        HccBoard board = new HccBoard();
        board.setName("genre");
        return board;
    }

    @Override
    public HccBoard setBoard(HccBoard board) throws HccUpdateException {
        // TODO Auto-generated method stub
        return null;
    }

}
