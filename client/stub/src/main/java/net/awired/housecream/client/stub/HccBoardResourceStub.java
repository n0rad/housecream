package net.awired.housecream.client.stub;

import net.awired.housecream.client.common.domain.board.HccBoard;
import net.awired.housecream.client.common.resource.HccUpdateException;
import net.awired.housecream.client.common.resource.client.HccBoardResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HccBoardResourceStub implements HccBoardResource {

    @Autowired
    private HccContext context;

    @Override
    public void setBoard(HccBoard board) throws HccUpdateException {
        // TODO Auto-generated method stub

    }

    @Override
    public void resetBoard() {
        // TODO Auto-generated method stub

    }

    @Override
    public void init() {
        // TODO Auto-generated method stub

    }

    @Override
    public void runNotify() {
        // TODO Auto-generated method stub

    }

    @Override
    public HccBoard getBoard() {
        // TODO Auto-generated method stub
        return null;
    }

}
